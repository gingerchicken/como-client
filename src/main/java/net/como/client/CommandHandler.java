package net.como.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.como.client.utils.*;

public class CommandHandler {
    public String delimiter;
    public int handle(String rawMessage, CallbackInfo ci) {
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
                String newRawState = args[1];

                switch (newRawState) {
                    case "enable": cheat.enable(); return 1;
                    case "disable": cheat.disable(); return 1;
                    default: {
                        CheatClient.displayChatMessage(String.format("%sInvalid Syntax: next time use \'.%s [enable|disable|blank for toggle]\'", ChatUtils.RED, command));
                        return 1;
                    }
                }
            }

            // If they have not provided any arguments we will assume that they just want to toggle the cheat.
            cheat.toggle();
            return 1;
        }

        return 0;
    }

    CommandHandler(String delimiter) {
        this.delimiter = delimiter;
    }
}
