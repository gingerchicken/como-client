package net.como.client.commands.structures;

import net.como.client.commands.settings.SettingsCommand;
import net.como.client.structures.Module;

public class ModuleCommand extends CommandNode {
    public Module cheat;

    public ModuleCommand(String command, Module cheat) {
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
