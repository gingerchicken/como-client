package net.como.client.commands;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.structures.Module;
import net.como.client.utils.ChatUtils;

import java.util.HashMap;

public class CommandChatIgnore extends Command {

    public CommandChatIgnore() {
        super("ignore", "Usage: ignore <phrase to ignore|reset|help>", "Ignore a specific phrase in chat");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean trigger(String[] args) {
        Module chatIgnore = ComoClient.Modules.get("chatignore");
        if (!chatIgnore.isEnabled()) {
            chatIgnore.displayMessage(String.format("%sChatIgnore is currently not enabled, enable it and try again.", ChatUtils.RED));
            return true;
        }

        // Handle help
        if (this.handleHelp(args)) return true;

        String input = String.join(" ", args).trim();

        HashMap<String, Boolean> phrases = (HashMap<String, Boolean>)chatIgnore.getSetting("Phrases").value;
        if (input.equals("reset")) {
            chatIgnore.displayMessage(
                "Successfully reset your ignore list."
            );

            phrases.clear();
        } else {
            chatIgnore.displayMessage(
                String.format("Successfully added phrase '%s.'", input)
            );

            phrases.put(input, true);
        }

        return true;
    }
}
