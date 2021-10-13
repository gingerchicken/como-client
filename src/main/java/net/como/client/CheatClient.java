package net.como.client;


import java.util.HashMap;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.cheats.*;
import net.como.client.commands.FriendsCommand;
import net.como.client.commands.PanicCommand;
import net.como.client.commands.WaypointsCommand;
import net.como.client.commands.structures.CheatCommand;
import net.como.client.commands.structures.CommandHandler;
import net.como.client.components.FriendsManager;
import net.como.client.utils.*;

import net.como.client.structures.Cheat;
import net.como.client.structures.events.EventEmitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class CheatClient {
    // Variables
    private static String CHAT_PREFIX = ChatUtils.chatPrefix("Como Client");
    public static CommandHandler commandHandler = new CommandHandler(".");
    public static EventEmitter emitter = new EventEmitter();
    public static FriendsManager friendsManager = new FriendsManager();

    public static GeneralConfig config;

    // Commands
    private static void registerCheatCommands() {
        // Add the friends command
        commandHandler.registerCommand(new FriendsCommand(friendsManager));

        // Add Panic command
        commandHandler.registerCommand(new PanicCommand());

        commandHandler.registerCommand(new WaypointsCommand(
            ((Waypoints)Cheats.get("waypoints")).waypoints
        ));

        // Add all of the cheats as commands.
        for (Entry<String, Cheat> entry : Cheats.entrySet()) {
            commandHandler.registerCommand(new CheatCommand(entry.getKey(), entry.getValue()));
        }
    }

    // Cheats
    public static HashMap<String, Cheat> Cheats = new HashMap<String, Cheat>();

    // Chat
    public static void processChatPost(String message, CallbackInfo ci) {
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
    }
    public static void displayChatMessage(String message) {
        ChatUtils.displayMessage(CHAT_PREFIX + message);
    }

    // Client
    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
    public static ClientPlayerEntity me() {
        // Get the client
        MinecraftClient client = getClient();

        // Return localplayer
        return client.player;
    }

    // Misc
    public static double getCurrentTime() {
        return Double.valueOf(System.currentTimeMillis()) / 1000d; // Seconds
    }

    public static void close() {
        System.out.println("Saving Client Config...");
        Persistance.saveConfig();

        System.out.println("It has been fun, remember to stay hydrated and that you matter <3");
    }

    public static void initialise() {
        System.out.println("Loading Como Client...");

        // Load up all the cheats
        Cheats.put("flight", new Flight());
        Cheats.put("blink", new Blink());
        Cheats.put("chatignore", new ChatIgnore());
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
        Cheats.put("autoreconnect", new AutoReconnect());
        Cheats.put("autorespawn", new AutoRespawn());
        Cheats.put("nofirecam", new NoFireCam());
        Cheats.put("killaura", new KillAura());
        Cheats.put("timer", new Timer());
        Cheats.put("criticals", new Criticals());
        Cheats.put("waypoints", new Waypoints());
        Cheats.put("homegodmode", new HomeGodMode());
        Cheats.put("itemrendertweaks", new ItemRenderTweaks());
        Cheats.put("tracers", new Tracers());
        Cheats.put("blockesp", new BlockESP());
        Cheats.put("betternametags", new BetterNameTags());
        Cheats.put("noportal", new NoPortal());

        // Load the config
        Persistance.loadConfig();
        
        config = new GeneralConfig();

        // Ready up all the commands
        registerCheatCommands();

        // Done!
        System.out.println("Como Client loaded!");
    }

    CheatClient() { }
}
