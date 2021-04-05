package net.como.client.structures;

import net.como.client.CheatClient;

public class Cheat {
    private String name;
    private boolean enabled;

    public Settings settings = new Settings();

    public String description;
    
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

    // Chat display message things
    public void onEnabled() { 
        activate();
        CheatClient.displayChatMessage(this.getName() + " is enabled.");
    }
    public void onDisabled() {
        deactivate();
        CheatClient.displayChatMessage(this.getName() + " is disabled.");
    }

    // Override me!
    public void activate() { }
    public void deactivate() { }
    public void recieveEvent(String event, Object[] args) { }

    protected Cheat(String name) {
        // Set the name
        this.name = name;
    }
}
