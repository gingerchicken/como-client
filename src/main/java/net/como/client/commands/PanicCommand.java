package net.como.client.commands;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.structures.Module;

public class PanicCommand extends Command {

    public PanicCommand() {
        super("panic", "panic", "Disables all of your active mods.");

        this.commandDisplay = "Panic";
    }

    @Override
    public Boolean trigger(String[] args) {
        // Make sure that the user knows what they are doing.
        if (args.length == 0 || !args[0].toLowerCase().startsWith("y")) {
            this.displayChatMessage("Are you sure you want to disable all your mods?");
            this.displayChatMessage("Usage: panic <y/N>");
            return true;
        }

        // Disable all
        for (Module cheat : ComoClient.Cheats.values()) {
            if (cheat.isEnabled()) cheat.disable();
        }
        this.displayChatMessage("All mods have been disabled.");
        return true;
    }
}
