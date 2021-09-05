package net.como.client.commands.settings.HashMapSettings;

import java.util.HashMap;

import net.como.client.commands.structures.Command;

public class HashMapSubCommand extends Command {
    HashMap<String, Boolean> refMap;

    public HashMapSubCommand(String command, String helpText, String description, HashMap<String, Boolean> refMap) {
        super(command, helpText, description);
        
        this.refMap = refMap;
    }
    
}
