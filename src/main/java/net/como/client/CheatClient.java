package net.como.client;


import java.util.HashMap;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.cheats.*;
import net.como.client.commands.CheatCommand;
import net.como.client.commands.CommandHandler;
import net.como.client.utils.*;

import net.como.client.structures.Cheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class CheatClient {
    private static String CHAT_PREFIX = ChatUtils.WHITE + "[" + ChatUtils.GREEN + "Como Client" + ChatUtils.WHITE + "] ";
    public static CommandHandler commandHandler = new CommandHandler(".");

    private static void registerCheatCommands() {
        // Add all of the cheats as commands.
        for (Entry<String, Cheat> entry : Cheats.entrySet()) {

            commandHandler.registerCommand(new CheatCommand(entry.getKey(), entry.getValue()));
        }
    }

    public static HashMap<String, Cheat> Cheats = new HashMap<String, Cheat>();
    static {
        Cheats.put("flight", new Flight());
        Cheats.put("blink", new Blink());
        Cheats.put("antichatbot", new AntiChatbot());
        Cheats.put("totemhide", new TotemHide());
        Cheats.put("entityesp", new EntityESP());
        Cheats.put("speed", new SpeedHack());
        Cheats.put("superjump", new SuperJump());
        Cheats.put("antiitemdrop", new AntiItemDrop());
        Cheats.put("noweather", new NoWeather());
        Cheats.put("nofall", new NoFall());
        Cheats.put("camflight", new CamFlight());
        Cheats.put("noboss", new NoBoss());
        Cheats.put("elytraflight", new ElytraFlight());
        Cheats.put("xray", new XRay());
        Cheats.put("noenchantbook", new NoEnchantmentBook());
        Cheats.put("nobreak", new NoBreak());
        Cheats.put("autoshear", new AutoShear());
        Cheats.put("tapemeasure", new TapeMeasure());
        Cheats.put("modlist", new ModList());
        Cheats.put("nohurtcam", new NoHurtCam());
        Cheats.put("fullbright", new FullBright());

        registerCheatCommands();
    }

    public static void triggerAllEvent(String eventName, Object[] args) {
        // Internal Events
        switch (eventName) {
            case "onPlayerChat": {
                String message  = (String)args[0];
                CallbackInfo ci = (CallbackInfo)args[1];

                // Command Handling
                Integer commandHandlerOutput = commandHandler.handle(message, ci);

                switch (commandHandlerOutput) {
                    case -1:
                        break;
                    
                    case 0: {
                        // TODO have it display the command's help text.
                        CheatClient.displayChatMessage(String.format("%sUnknown Command: Use 'help' for a list of commands.", ChatUtils.RED));
                    }

                    default: {
                        ci.cancel();
                    }
                }

                System.out.println(commandHandlerOutput);
            }
        }

        // TODO Maybe make listeners instead?
        Cheats.forEach((String name, Cheat cheat) -> {
            if (cheat.isEnabled())
                cheat.recieveEvent(eventName, args);
        });
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static void displayChatMessage(String message) {
        System.out.println(message);
        ChatUtils.displayMessage(CHAT_PREFIX + message);
    }

    public static ClientPlayerEntity me() {
        // Get the client
        MinecraftClient client = getClient();

        // Return localplayer
        return client.player;
    }

    CheatClient() {

    }
}
