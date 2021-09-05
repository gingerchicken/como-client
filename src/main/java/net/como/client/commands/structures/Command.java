package net.como.client.commands.structures;

import net.como.client.CheatClient;
import net.como.client.utils.ChatUtils;

public class Command {
    private String command;
    private String helpText;
    private String description = "A really cool command!";

    public String getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHelpText() {
        return this.helpText;
    }

    public Command(String command, String helpText, String description) {
        this.command = command;
        this.helpText = helpText;
        this.description = description;
    }

    public boolean shouldShowHelp(String[] args) {
        return args.length == 0 || (args.length == 1 && args[0].toLowerCase().equals("help"));
    }

    public Boolean handleHelp(String[] args) {
        if (!this.shouldShowHelp(args)) return false;

        CheatClient.displayChatMessage(String.format("%s%s", ChatUtils.WHITE, this.getHelpText()));
        return true;
    }

    // Returning false should give display the help text.
    // TODO make a raw message thing too.
    public Boolean trigger(String[] args) {
        return handleHelp(args); 
    }
}