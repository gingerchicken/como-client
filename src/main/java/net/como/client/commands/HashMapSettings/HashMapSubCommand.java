package net.como.client.commands.HashMapSettings;

import java.util.HashMap;

import net.como.client.commands.Command;

public class HashMapSubCommand extends Command {
    HashMap<String, Boolean> refMap;

    public HashMapSubCommand(String command, String helpText, String description, HashMap<String, Boolean> refMap) {
        super(command, helpText, description);
        
        this.refMap = refMap;
    }
    
}
