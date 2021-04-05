package net.como.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.como.client.utils.*;

public class CommandHandler {
    public String delimiter;
    public int handle(String rawMessage, CallbackInfo ci) {
        // TODO Just replace this tbf - make a better command system.

        // -1 unhandled but not command
        if (!rawMessage.startsWith(this.delimiter)) return -1;

        ci.cancel();
        String[] args = rawMessage.split(" ");
        String command = args[0].substring(1).toLowerCase();

        // Check for cheats with that name
        Cheat cheat = CheatClient.Cheats.get(command);
        if (cheat != null) {
            // Maybe they want to set the specific state of the cheat.
            if (args.length > 1) {
                String mode = args[1];

                switch (mode) {
                    case "enable": cheat.enable(); return 1;
                    case "disable": cheat.disable(); return 1;
                    case "setting": {
                        String settingName = (args.length > 2) ? args[2] : null;
                        
                        Setting setting = cheat.settings.getSetting(settingName);
                        if (setting == null) {
                            return 1;
                        }

                        String value = (args.length > 3) ? args[3] : null;

                        if (value == null) {
                            CheatClient.displayChatMessage(String.format("%sInvalid Syntax: next time use \'.%s setting %s [new value]\'", ChatUtils.RED, command, settingName));
                            return 1;
                        }

                        switch (setting.value.getClass().getName()) {
                            case "java.lang.Boolean": {
                                boolean realValue = value.equals("true");
                                // System.out.println(value.equals("true"));
                                
                                setting.value = realValue;
                                System.out.println(setting.value);

                                return 0;
                            }
                            case "java.lang.Double": {
                                Double realValue = null;
                                
                                try {
                                    realValue = Double.valueOf(value);
                                } catch (Exception e) { }

                                if (realValue == null) {
                                    CheatClient.displayChatMessage(String.format("%sInvalid Value: recieved NaN as a parameter", ChatUtils.RED));
                                    return 1;
                                }
                                
                                setting.value = realValue;
                            }

                        }
                        return 1;
                    }
                    default: {
                        CheatClient.displayChatMessage(String.format("%sInvalid Syntax: next time use \'.%s [enable|setting|disable|blank for toggle]\'", ChatUtils.RED, command));
                        return 1;
                    }
                }
            }

            // If they have not provided any arguments we will assume that they just want to toggle the cheat.
            cheat.toggle();
            return 1;
        }

        CheatClient.displayChatMessage(ChatUtils.RED + "Unknown command \'" + command + "\'");
        return 0;
    }

    CommandHandler(String delimiter) {
        this.delimiter = delimiter;
    }
}
