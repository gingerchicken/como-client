package net.como.client.commands.settings.HashMapSettings;

import java.util.HashMap;

public class HashMapCommandRemove extends HashMapSubCommand {

    public HashMapCommandRemove(HashMap<String, Boolean> refMap) {
        super("remove", "remove [key to be removed]", "Remove an item from the hash map", refMap);
    }
    
    @Override
    public Boolean trigger(String[] args) {
        if (args.length == 0) return super.trigger(args);

        // Get the key
        String key = String.join(" ", args);

        // Remove the key (whether it is there or not!)
        this.refMap.remove(key);

        // Everything's all good!
        return true;
    }
}
