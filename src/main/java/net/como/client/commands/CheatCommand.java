package net.como.client.commands;

import net.como.client.structures.Cheat;

public class CheatCommand extends CommandNode {
    public Cheat cheat;

    public CheatCommand(String command, Cheat cheat) {
        super(command, cheat.description);
        
        this.cheat = cheat;

        // Register the settings.
        super.addSubCommand(new SettingsCommand(this.cheat));
    }

    @Override
    public Boolean trigger(String[] args) {
        if (args.length == 0) {
            // Toggle the cheat.

            cheat.toggle();
            return true;
        }

        // Handle help
        if (handleHelp(args)) return true;

        return super.trigger(args);
    }
}
