package net.como.client.commands;

import java.util.Arrays;
import java.util.HashMap;

public class CommandNode extends Command {
    HashMap<String, Command> subCommands = new HashMap<String, Command>();

    public CommandNode(String command, String description) {
        super(command, "Sorry, this command doesn't do anything of interest!", description);
    }

    public void addSubCommand(Command subCommand) {

        // The amount of times I have wrote the following instead of the stupid Java way of doing it!
        // subCommands[subCommand.getCommand()] = subCommand;

        subCommands.put(subCommand.getCommand(), subCommand);
    }

    // TODO make this look nicer.
    @Override
    public String getHelpText() {
        String text = "List of sub commands: ";
        for (Command cmd : subCommands.values()) {
            text += "\n" + cmd.getCommand() + " - " + cmd.getDescription();
        }

        return text;
    }

    @Override
    public Boolean trigger(String[] args) {
        // Make sure there is a given sub command else just display the help text.
        if (args.length == 0) return false;

        // Handle help
        if (handleHelp(args)) return true;

        // Get the command
        String command = args[0];

        // Make sure that the command exists.
        if (!this.subCommands.containsKey(command)) return false;

        // Get the arguments
        String[] subArguments = Arrays.copyOfRange(args, 1, args.length);

        return subCommands.get(command).trigger(subArguments);
    }
}
