package net.como.client.modules.chat;

import java.util.ArrayList;
import java.util.List;

import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandHandler;
import net.como.client.commands.structures.CommandNode;
import net.como.client.structures.Module;
import net.como.client.utils.ChatUtils;

public class CommandAutoFill extends Module {

    @Override
    public boolean shouldDisplayInModList() {
        return false;
    }

    public CommandAutoFill() {
        super("ChatSuggestion", true);

        this.setDescription("Allows you to have command auto-fill in chat.");

        this.setCategory("Chat");
    }

    // TODO Unit test this please!
    // This is hear to make debugging a little easier since I cannot place breakpoints inside of mixins (at least in my IDE)
    public List<String> getSuggestions(String text, CommandHandler handler) {        
        // It must be the first part!
        if (!text.contains(" ")) {
            List<String> commands = new ArrayList<>();
            for (String key : handler.getCommandSet()) {
                // Make sure that hey are start with a .
                commands.add(handler.delimiter.concat(key));
            }
            return commands;
        }

        // Remove the . or ,
        text = text.startsWith(handler.delimiter) ? text.substring(1) : text;

        int workingStart = ChatUtils.getStartOfCurrentWord(text);

        // Get the different parts
        String[] parts = text.substring(0, workingStart).split(" ");

        // Make sure we have at least one part
        if (parts.length == 0) return new ArrayList<String>();

        // This will be used to start of the CommandNode search
        Command currentCmd = handler.getCommand(parts[0]);
        int i; // This will be later used to see if we checked all nodes

        // We want to get the last command parts so we can ignore the last part (i.e. the thing the user will be typing)
        for (i = 1; i < parts.length; i++) {
            // Make sure that we can follow the chain
            if (currentCmd == null || !(currentCmd instanceof CommandNode)) break;

            // Get the current command as a command node
            CommandNode node = (CommandNode)(currentCmd);

            // Get the next command in the chain
            Command cmd = node.getSubCommand(parts[i]);

            // Next node!
            currentCmd = cmd;
        }

        // Check if we finished
        Boolean didFinsh = i == parts.length;
        
        // If the command isn't recognised at any point, just return an empty set.
        if (currentCmd == null || !didFinsh) {
            return new ArrayList<>();
        }

        // Else get the suggestions
        return currentCmd.getSuggestions();
    }
}
