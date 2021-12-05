package net.como.client.structures;

import java.util.HashMap;

import com.google.gson.Gson;

import net.como.client.ComoClient;
import net.como.client.interfaces.Flatternable;
import net.como.client.utils.ChatUtils;
import net.minecraft.client.font.TextRenderer;
import net.como.client.structures.settings.*;


import net.como.client.structures.events.EventListener;
import net.como.client.structures.events.Event;
import net.como.client.structures.events.EventEmitter;

public class Module extends Settings implements EventListener, Flatternable {
    private String name;
    private boolean enabled;

    public String description;
    public boolean modListDisplay = true;
    
    public void displayMessage(String msg) {
        ChatUtils.displayMessage(
            String.format("%s%s", ChatUtils.chatPrefix(this.getName()), msg)
        );
    }

    // This is to do with displaying the item in the list.
    public String listOption() {
        return null;
    }

    public Boolean hasListOption() {
        return this.listOption() != null;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
        this.onEnabled();
    }

    public void disable() {
        this.enabled = false;
        this.onDisabled();
    }

    public void toggle() {
        this.enabled = !this.enabled;

        // Make sure that the potential overrides are handled.
        if (this.enabled) this.onEnabled();
        else this.onDisabled();
    }

    public boolean shouldDisplayInModList() {
        return this.modListDisplay && this.enabled;
    }

    // Chat display message things
    public void onEnabled() { 
        ComoClient.displayChatMessage(
            String.format("%s has been %senabled%s.", this.getName(), ChatUtils.GREEN, ChatUtils.WHITE)
        );
        activate();
    }
    public void onDisabled() {
        ComoClient.displayChatMessage(
            String.format("%s has been %sdisabled%s.", this.getName(), ChatUtils.RED, ChatUtils.WHITE)
        );
        deactivate();
    }

    // Override me!
    public void activate() { }
    public void deactivate() { }

    protected Module(String name) {
        // Set the name
        this.name = name;
    }

    private Boolean autoEnable = false;
    protected Module(String name, Boolean autoEnable) {
        this.name = name;
        this.autoEnable = autoEnable;
    }

    public Boolean shouldAutoEnable() {
        return this.autoEnable;
    }

    // Events
    private EventEmitter emitter = ComoClient.emitter;

    @Override
    public void addListen(Class<? extends Event> event) {
        this.emitter.addListener(this, event);
    }

    @Override
    public void removeListen(Class<? extends Event> event) {
        this.emitter.removeListener(this, event);
    }

    @Override
    public void fireEvent(Event event) {
        // TODO Auto-generated method stub
        
    }

    public HashMap<String, String> flatten() {
        HashMap<String, String> data = new HashMap<String, String>();
        Gson gson = new Gson();

        for (String name : this.getSettings()) {
            Setting setting = this.getSetting(name);

            // This will mean that their type will be lost in the JSON file however, I cannot think of a nice way around it.
            data.put(name, gson.toJson(setting.value));
        }

        data.put("enabled", this.isEnabled().toString());

        return data;
    }

    public void lift(HashMap<String, String> data) {
        Gson gson = new Gson();

        boolean enable = data.get("enabled").equals("true");
        data.remove("enabled");

        for (String name : data.keySet()) {
            Setting setting = this.getSetting(name);

            // Make sure that the setting is valid
            if (setting == null) {
                System.out.println(String.format("Unknown setting '%s' in mod '%s.'", name, this.getName()));
                continue;
            }

            setting.value = gson.fromJson(data.get(name), setting.value.getClass());
        }

        if (enable) this.enable();
    }

    public int getTextWidth(TextRenderer textRenderer) {
        int w = textRenderer.getWidth(this.getName());

        w += (this.hasListOption() ? textRenderer.getWidth(String.format("[%s]", this.listOption())) + 2 : 0);

        return w;
    }
}
