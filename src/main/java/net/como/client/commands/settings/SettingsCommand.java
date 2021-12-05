package net.como.client.commands.settings;

import java.util.Arrays;
import java.util.HashMap;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.structures.settings.*;
import net.como.client.utils.ChatUtils;

public class SettingsCommand extends Command {
    private Settings settings;

    public SettingsCommand(Settings settings) {
        super("settings", "settings <list|help|...setting> <...>", "Change settings 'n' stuff.");

        this.settings = settings;
    }

    @SuppressWarnings("unchecked")
    private boolean changeSetting(String[] args) {
        // Get the setting name.
        String settingName = args[0];

        // Make sure that it exists.
        if (!settings.settingExists(settingName)) return false;

        // Get the setting
        Setting setting = settings.getSetting(settingName);

        // Get the new value (if there is one)
        String value = (args.length > 1) ? args[1] : null;

        // If they didn't provide a new value, just show them what it is currently.
        if (value == null) {
            ComoClient.displayChatMessage(String.format("%sSetting Value: %s", ChatUtils.WHITE, setting.value.toString()));
            return true;
        }

        // Update the setting in a horrible way.
        switch (setting.value.getClass().getName()) {
            case "java.lang.String": {
                setting.value = value;
                
                return true;
            }
            case "java.lang.Boolean": {
                boolean realValue = value.equals("true");
                
                setting.value = realValue;

                return true;
            }
            case "java.lang.Float": {
                // TODO this is legit just a copy of the double thing, make this nicer I swear!
                Float realValue = null;
                
                try {
                    realValue = Float.valueOf(value);
                } catch (Exception e) { }

                if (realValue == null) {
                    ComoClient.displayChatMessage(String.format("%sInvalid Value: recieved NaN as a parameter", ChatUtils.RED));
                    return true;
                }
                
                setting.value = realValue;

                return true;
            }
            // TODO come up with something better, this one is alot like the one underneath.
            case "java.lang.Double": {
                Double realValue = null;
                
                try {
                    realValue = Double.valueOf(value);
                } catch (Exception e) { }

                if (realValue == null) {
                    ComoClient.displayChatMessage(String.format("%sInvalid Value: recieved NaN as a parameter", ChatUtils.RED));
                    return true;
                }
                
                setting.value = realValue;

                return true;
            }
            case "java.lang.Integer": {
                Integer realValue = null;

                try {
                    realValue = Integer.valueOf(value);
                } catch (Exception e) { }

                if (realValue == null) {
                    ComoClient.displayChatMessage(String.format("%sInvalid Value: recieved NaN as a parameter", ChatUtils.RED));
                    return true;
                }
                
                setting.value = realValue;

                return true;
            }
            case "java.util.HashMap": {
                // Pretty sure 9/10 times it is this type but like please check in future.
                HashMap<String, Boolean> map = (HashMap<String, Boolean>)setting.value;

                // Generate a new command and trigger it.
                // TODO this is now how this is meant to be used but I cannot be bothered to do it another way, please be bothered at some point.
                HashMapCommand hashCommands = new HashMapCommand(map);

                // Return the result
                return hashCommands.trigger(Arrays.copyOfRange(args, 1, args.length));
            }
        }

        // Unsupported setting type
        return false;
    } 

    private boolean displayList(String[] args) {
        ComoClient.displayChatMessage("Current Settings:");
        for (String name : settings.getSettings()) {
            ComoClient.displayChatMessage(
                String.format("-> %s - %s", name, settings.getSetting(name).value.toString())
            );
        }

        return true;
    }

    @Override
    public Boolean trigger(String[] args) {
        if (this.handleHelp(args)) return true;

        if (args.length == 0) {
            ComoClient.displayChatMessage(String.format("%s%s", ChatUtils.WHITE, this.getHelpText()));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list": {
                return this.displayList(args);
            }
            // It might be a setting?
            default: {
                return this.changeSetting(args);
            }
        }
    }
}
