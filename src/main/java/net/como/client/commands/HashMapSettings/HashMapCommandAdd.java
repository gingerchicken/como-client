package net.como.client.commands.HashMapSettings;

import java.util.HashMap;

public class HashMapCommandAdd extends HashMapSubCommand {

    public HashMapCommandAdd(HashMap<String, Boolean> refMap) {
        super("add", "remove [key to be added]", "Add an item to the hash map", refMap);
    }
    
    @Override
    public Boolean trigger(String[] args) {
        if (args.length == 0) return super.trigger(args);

        // Get the value
        String value = String.join(" ", args);

        // Add the value to the array
        this.refMap.put(value, true);

        // Everything's all good!
        return true;
    }
}
