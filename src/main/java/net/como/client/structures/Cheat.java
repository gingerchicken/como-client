package net.como.client.structures;

import net.como.client.CheatClient;
import net.como.client.utils.ChatUtils;

public class Cheat extends Settings {
    private String name;
    private boolean enabled;

    public String description;
    public boolean modListDisplay = true;
    
    public void displayMessage(String msg) {
        ChatUtils.displayMessage(String.format("%s[%s%s%s] %s", ChatUtils.WHITE, ChatUtils.GREEN, this.getName(), ChatUtils.WHITE, msg));
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
    public void receiveEvent(String event, Object[] args) { }

    protected Cheat(String name) {
        // Set the name
        this.name = name;
    }
}
