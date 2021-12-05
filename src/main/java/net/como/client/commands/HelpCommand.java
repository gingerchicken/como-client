package net.como.client.commands;

import java.util.HashMap;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.utils.ChatUtils;


public class HelpCommand extends Command {
    public HelpCommand(HashMap<String, Command> refCommands) {
        super("help", "If you need help with help then get help.", "Lists all the commands with their descriptions.");
    
        this.refCommands = refCommands;
    }

    @Override
    public Boolean trigger(String[] args) {
        String displayText = "All of the commands:";

        // Gather the information.
        Integer i = 1;
        for (HashMap.Entry<String, Command> entry : refCommands.entrySet()) {
            displayText += String.format("\n%s%s%s - %s", ChatUtils.GREEN, entry.getKey(), ChatUtils.WHITE, entry.getValue().getDescription());

            i++;
        }

        // Tell them how it be.
        ComoClient.displayChatMessage(String.format("%s%s", ChatUtils.WHITE, displayText));

        // How can help go wrong?!
        return true;
    }

    private HashMap<String, Command> refCommands;

    
}
