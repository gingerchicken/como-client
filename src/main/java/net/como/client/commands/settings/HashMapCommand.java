package net.como.client.commands.settings;

import java.util.HashMap;

import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;

public class HashMapCommand extends CommandNode {
    private static class SubCommand extends Command {
        public HashMap<String, Boolean> refMap;
    
        public SubCommand(String command, String helpText, String description, HashMap<String, Boolean> refMap) {
            super(command, helpText, description);
            
            this.refMap = refMap;
        }
    }

    private static class Add extends SubCommand {

        public Add(HashMap<String, Boolean> refMap) {
            super("add", "remove [key to be added]", "Add an item to the hash map", refMap);
        }
        
        @Override
        public Boolean trigger(String[] args) {
            if (args.length == 0) return super.trigger(args);
    
            // Add the value to the array
            this.refMap.put(this.combineArgs(args), true);
            return true;
        }
    }

    private static class Remove extends SubCommand {

        public Remove(HashMap<String, Boolean> refMap) {
            super("remove", "remove [key to be removed]", "Remove an item from the hash map", refMap);
        }
        
        @Override
        public Boolean trigger(String[] args) {
            if (args.length == 0) return super.trigger(args);
    
            // Remove the key (whether it is there or not!)
            this.refMap.remove(this.combineArgs(args));
            return true;
        }
    }
    

    public HashMapCommand(String command, HashMap<String, Boolean> refMap) {
        super(command, "Adjust a map");

        // Add sub commands.
        this.addSubCommand(new Add(refMap));
        this.addSubCommand(new Remove(refMap));
    }
    
}
