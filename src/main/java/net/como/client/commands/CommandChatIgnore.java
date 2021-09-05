package net.como.client.commands;

import net.como.client.CheatClient;
import net.como.client.commands.structures.Command;
import net.como.client.structures.Cheat;
import net.como.client.utils.ChatUtils;

import java.util.HashMap;

public class CommandChatIgnore extends Command {

    public CommandChatIgnore() {
        super("ignore", "Usage: ignore <phrase to ignore|reset>", "Ignore a specific phrase in chat");
    }
    
    @Override
    public Boolean trigger(String[] args) {
        Cheat chatIgnore = CheatClient.Cheats.get("chatignore");
        if (!chatIgnore.isEnabled()) {
            chatIgnore.displayMessage(ChatUtils.RED + "ChatIgnore is currently not enabled, enable it and try again.");
            return true;
        }

        String input = String.join(" ", args).trim();

        // Make sure that the user doesn't need help
        if (input.length() == 0) return this.handleHelp(args);

        if (input.equals("reset")) {
            chatIgnore.displayMessage(
                "Successfully reset your ignore list."
            );
        } else {
            HashMap<String, Boolean> phrases = (HashMap<String, Boolean>)chatIgnore.getSetting("Phrases").value;
            phrases.put(input, true);

            chatIgnore.displayMessage(
                String.format("Successfully added phrase '%s.'", input)
            );
        }

        return true;
    }
}
