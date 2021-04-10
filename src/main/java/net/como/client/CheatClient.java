package net.como.client;


import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.cheats.*;
import net.como.client.utils.*;

import net.como.client.structures.Cheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class CheatClient {
    private static String CHAT_PREFIX = ChatUtils.WHITE + "[" + ChatUtils.GREEN + "ComoClient" + ChatUtils.WHITE + "] ";
    public static CommandHandler commandHandler = new CommandHandler(".");

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
    }

    public static void triggerAllEvent(String eventName, Object[] args) {
        // Internal Events
        switch (eventName) {
            case "onPlayerChat": {
                String message  = (String)args[0];
                CallbackInfo ci = (CallbackInfo)args[1];

                commandHandler.handle(message, ci);
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
