package net.como.client.modules;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.config.Flatternable;
import net.como.client.config.settings.*;
import net.como.client.config.specials.Mode;
import net.como.client.events.Event;
import net.como.client.events.EventEmitter;
import net.como.client.events.EventListener;
import net.como.client.utils.ChatUtils;
import net.minecraft.client.font.TextRenderer;

public abstract class Module extends Settings implements EventListener, Flatternable {
    private String name;
    private boolean enabled;
    private String category = "Misc";

    /**
     * Gets the module's category
     * @return The module's category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Sets the module's category
     * @param category The new category
     */
    protected void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets all of the module's commands
     * @return All of the module's commands
     */
    public Iterable<Command> getCommands() {
        return new ArrayList<Command>();
    }

    /**
     * Description of the module
     */
    private String description;

    /**
     * Gets the module's description
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the module's description
     * @param description The new description
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    /**
     * Should the item be displayed in the mod list
     */
    protected boolean modListDisplay = true;

    /**
     * Wraps a chat message in a coloured prefix with the module's name
     * @param msg The message to wrap
     */
    public void displayMessage(String msg) {
        ChatUtils.displayMessage(
            String.format("%s%s", ChatUtils.chatPrefix(this.getName()), msg)
        );
    }

    // This is to do with displaying the item in the list.

    /**
     * Gets the subtext for the mod list.
     * @return The subtext for the mod list.
     */
    public String listOption() {
        return null;
    }

    /**
     * Checks if the module has a list option
     * @return True if the module has a list option, false otherwise.
     */
    public boolean hasListOption() {
        return this.listOption() != null;
    }

    // Getters and setters

    /**
     * Gets the module's name.
     * @return The module's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the module is enabled.
     * @return true if the module is enabled.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Enables the module
     */
    public void enable() {
        this.enabled = true;
        this.onEnabled();
    }

    /**
     * Disables the module
     */
    public void disable() {
        this.enabled = false;
        this.onDisabled();
    }

    /**
     * Toggles if the module is enabled or disabled
     */
    public void toggle() {
        this.enabled = !this.enabled;

        // Make sure that the potential overrides are handled.
        if (this.enabled) this.onEnabled();
        else this.onDisabled();
    }

    /**
     * Checks if the module should be displayed in the mod list
     * @return
     */
    public boolean shouldDisplayInModList() {
        return this.modListDisplay && this.enabled;
    }

    // Chat display message things

    /**
     * Called when the module is enabled
     */
    public void onEnabled() { 
        ComoClient.displayChatMessage(
            String.format("%s has been %senabled%s.", this.getName(), ChatUtils.GREEN, ChatUtils.WHITE)
        );

        activate();
    }

    /**
     * Called when the module is disabled
     */
    public void onDisabled() {
        ComoClient.displayChatMessage(
            String.format("%s has been %sdisabled%s.", this.getName(), ChatUtils.RED, ChatUtils.WHITE)
        );

        deactivate();
    }

    // Override me!
    public abstract void activate();
    public abstract void deactivate();

    /**
     * Initialise a new module with a given name
     * @param name The name of the module
     */
    protected Module(String name) {
        // Set the name
        this.name = name;
    }

    /**
     * Flag for if the module should be enabled with a fresh configuration
     */
    private boolean autoEnable = false;

    /**
     * Initalise a module with an autoEnable flag
     * @param name The name of the module
     * @param autoEnable The autoEnable flag
    */
    protected Module(String name, boolean autoEnable) {
        this.name = name;
        this.autoEnable = autoEnable;
    }

    /**
     * Gets if the module should be auto enabled
     * @return True if the module should be auto enabled, false otherwise.
     */
    public boolean shouldAutoEnable() {
        return this.autoEnable;
    }

    // Events
    /**
     * Reference to the event emitter
     */
    private EventEmitter emitter = ComoClient.getInstance().emitter;

    @Override
    public void addListen(Class<? extends Event> event) {
        this.emitter.addListener(this, event);
    }

    @Override
    public void removeListen(Class<? extends Event> event) {
        this.emitter.removeListener(this, event);
    }

    /**
     * Converts each setting to a string for saving in the config file
     */
    public HashMap<String, String> flatten() {
        HashMap<String, String> data = new HashMap<String, String>();
        Gson gson = new Gson();

        for (String name : this.getSettings()) {
            Setting setting = this.getSetting(name);

            String val = null;
            if (setting.value instanceof Mode) {
                Mode mode = this.getModeSetting(name);
                
                val = mode.getStateName();
            }
            val = val == null ? gson.toJson(setting.value) : val;

            // This will mean that their type will be lost in the JSON file however, I cannot think of a nice way around it.
            data.put(name, val);
        }

        data.put("enabled", String.valueOf(this.isEnabled()));

        return data;
    }

    /**
     * Converts a string to settings for loading from the config file
     */
    public void lift(HashMap<String, String> data) {
        Gson gson = new Gson();

        boolean enable = data.get("enabled").equals("true");
        data.remove("enabled");

        for (String name : data.keySet()) {
            Setting setting = this.getSetting(name);

            // Make sure that the setting is valid
            if (setting == null) {
                ComoClient.log(String.format("Unknown setting '%s' in mod '%s.'", name, this.getName()));
                continue;
            }

            // Handle Modes
            if (setting.value instanceof Mode) {
                Mode mode = this.getModeSetting(name);
                String val = data.get(name);

                if (!mode.setState(val)) {
                    ComoClient.log(String.format("Invalid state '%s' for setting '%s' in mod '%s.'", val, name, this.getName()));
                    continue;
                }

                continue;
            }

            setting.value = gson.fromJson(data.get(name), setting.value.getClass());
        }

        if (enable) this.enable();
    }

    /**
     * Gets the text width for the module to be displayed in the mod list
     * @param textRenderer The text renderer
     * @return The text width for the module to be displayed in the mod list
     */
    public int getTextWidth(TextRenderer textRenderer) {
        int w = textRenderer.getWidth(this.getName());

        w += (this.hasListOption() ? textRenderer.getWidth(String.format("[%s]", this.listOption())) + 2 : 0);

        return w;
    }
}
