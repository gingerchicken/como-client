package net.como.client.commands.structures;

import java.util.Arrays;
import java.util.HashMap;

import net.como.client.CheatClient;
import net.minecraft.util.ChatUtil;

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
    public Boolean handleHelp(String[] args) {
        if (!this.shouldShowHelp(args)) return false;

        CheatClient.displayChatMessage("List of sub commands: ");
        for (Command cmd : subCommands.values()) {
            CheatClient.displayChatMessage(
                String.format("-> %s - %s", cmd.getCommand(), cmd.getDescription())
            );
        }

        return true;
    }

    @Override
    public Boolean trigger(String[] args) {
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
