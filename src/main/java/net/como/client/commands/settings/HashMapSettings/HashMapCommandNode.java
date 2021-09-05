package net.como.client.commands.settings.HashMapSettings;

import java.util.HashMap;

import net.como.client.commands.structures.CommandNode;

public class HashMapCommandNode extends CommandNode {
    HashMap<String, Boolean> refMap;

    public HashMapCommandNode(String command, HashMap<String, Boolean> refMap) {
        super(command, "Adjust a map");
        
        this.refMap = refMap;

        this.addSubCommand(new HashMapCommandAdd(refMap));
        this.addSubCommand(new HashMapCommandRemove(refMap));
    }
    
}
