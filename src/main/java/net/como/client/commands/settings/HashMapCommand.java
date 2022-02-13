package net.como.client.commands.settings;

import java.util.HashMap;

import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ChatUtils;

public class HashMapCommand extends CommandNode {
    private static class SubCommand extends Command {
        public HashMap<String, Boolean> refMap;
        private String settingName; 
    
        public String getSettingName() {
            return this.settingName;
        }

        public SubCommand(String command, String helpText, String description, Setting setting) {
            super(command, helpText, description);
            
            this.settingName = setting.name;
            this.refMap = (HashMap<String, Boolean>)setting.value;
        }
    }

    private static class Add extends SubCommand {

        public Add(Setting setting) {
            super("add", "remove [key to be added]", "Add an item to the hash map", setting);
        }
        
        @Override
        public Boolean trigger(String[] args) {
            if (args.length == 0) return super.trigger(args);
    
            // Get the value
            String val = this.combineArgs(args);

            // Add the value to the array
            this.refMap.put(val, true);
            
            // Say we did it.
            this.displayChatMessage(String.format("Added %s%s%s to %s.", ChatUtils.GREEN, val, ChatUtils.WHITE, super.getSettingName()));
            
            return true;
        }
    }

    private static class Remove extends SubCommand {

        public Remove(Setting setting) {
            super("remove", "remove [key to be removed]", "Remove an item from the hash map", setting);
        }
        
        @Override
        public Boolean trigger(String[] args) {
            if (args.length == 0) return super.trigger(args);

            // Get the value
            String val = this.combineArgs(args);

            // Remove the key (whether it is there or not!)
            this.refMap.remove(this.combineArgs(args));
            
            // Say we did it.
            this.displayChatMessage(String.format("Removed %s%s%s from %s.", ChatUtils.RED, val, ChatUtils.WHITE, super.getSettingName()));

            return true;
        }
    }
    

    public HashMapCommand(Setting setting) {
        super(setting.name, "Adjust a map");

        // Add sub commands.
        this.addSubCommand(new Add(setting));
        this.addSubCommand(new Remove(setting));
    }
    
}
