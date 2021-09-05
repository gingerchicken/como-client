package net.como.client.commands.structures;

import java.util.Arrays;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.commands.HelpCommand;

public class CommandHandler {
    public String delimiter;
    
    private HashMap<String, Command> commands;

    public int handle(String rawMessage, CallbackInfo ci) {
        // -1 unhandled but not command
        if (!rawMessage.startsWith(this.delimiter)) return -1;

        // Get all of the words as an array.
        String[] rawSplit = rawMessage.split(" ");

        // Get the args but without the first command.
        String[] args = Arrays.copyOfRange(rawSplit, 1, rawSplit.length);

        // Get the command as a string
        String commandStr = rawSplit[0].substring(1);
        
        // Check if the command exists.
        if (!this.commands.containsKey(commandStr)) return 0;

        // Why must I suffer?
        Boolean output = this.commands.get(commandStr).trigger(args);

        // I hate Java
        return output ? 1 : 0;
    }

    public void registerCommand(Command command) {
        this.commands.put(command.getCommand(), command);
    }

    public CommandHandler(String delimiter) {
        this.delimiter = delimiter;

        // Initalise the commands *BEFORE* we give it as a reference.
        this.commands = new HashMap<String, Command>();

        // Add a help command because we all need help sometimes :3
        this.registerCommand(new HelpCommand(this.commands));
    }
}
