package net.como.client.structures;

import net.como.client.CheatClient;
import net.como.client.utils.ChatUtils;
import net.como.client.structures.settings.*;


import net.como.client.structures.events.EventListener;
import net.como.client.structures.events.Event;
import net.como.client.structures.events.EventEmitter;

public class Cheat extends Settings implements EventListener {
    private String name;
    private boolean enabled;

    public String description;
    public boolean modListDisplay = true;
    
    public void displayMessage(String msg) {
        ChatUtils.displayMessage(
            String.format("%s %s", ChatUtils.chatPrefix(this.getName()), msg)
        );
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
        CheatClient.displayChatMessage(
            String.format("%s has been enabled.", this.getName())
        );
        activate();
    }
    public void onDisabled() {
        CheatClient.displayChatMessage(
            String.format("%s has been disabled.", this.getName())
        );
        deactivate();
    }

    // Override me!
    public void activate() { }
    public void deactivate() { }

    protected Cheat(String name) {
        // Set the name
        this.name = name;
    }

    // Events
    private EventEmitter emitter = CheatClient.emitter;

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

}
