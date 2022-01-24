package net.como.client;


import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.commands.FontCommand;
import net.como.client.commands.FriendsCommand;
import net.como.client.commands.PanicCommand;
import net.como.client.commands.WaypointsCommand;
import net.como.client.commands.structures.ModuleCommand;
import net.como.client.commands.structures.CommandHandler;
import net.como.client.components.FriendsManager;
import net.como.client.interfaces.mixin.IClient;
import net.como.client.interfaces.mixin.IFontManager;
import net.como.client.modules.chat.*;
import net.como.client.modules.combat.*;
import net.como.client.modules.dupes.*;
import net.como.client.modules.exploits.*;
import net.como.client.modules.hud.ArmourDisplay;
import net.como.client.modules.hud.ClickGUI;
import net.como.client.modules.hud.ModList;
import net.como.client.modules.hud.Watermark;
import net.como.client.modules.movement.*;
import net.como.client.modules.packet.*;
import net.como.client.modules.render.*;
import net.como.client.modules.utilities.*;
import net.como.client.utils.*;
import net.fabricmc.loader.api.FabricLoader;
import net.como.client.structures.Module;
import net.como.client.structures.events.EventEmitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ComoClient {
    // Variables
    private static String CHAT_PREFIX = ChatUtils.chatPrefix("Como Client");
    public static CommandHandler commandHandler;
    public static EventEmitter emitter = new EventEmitter();
    public static FriendsManager friendsManager = new FriendsManager();
    public static TextRenderer textRenderer;

    public static GeneralConfig config;
    private static String fontId = new String();

    private static Logger logger = LogManager.getLogger("Como Client");
    public static void log(Object obj) {
        log(obj.toString());
    }
    public static void log(String str) {
        // I don't want no rats
        str = str.replaceAll("jndi:ldap", "sug:ma");

        logger.info(str);
    }

    public static void updateFont(String id) {
        if (fontId.equals(id)) return;

        fontId = id;
        textRenderer = createTextRenderer();
    }

    public static TextRenderer createTextRenderer() {
        IClient client = (IClient)getClient();
        IFontManager fontManager = (IFontManager)client.getFontManager();

        return fontManager.createTextRendererFromIdentifier(new Identifier(fontId));
    }

    // Commands
    private static void registerModuleCommands() {
        // Add the font command
        commandHandler.registerCommand(new FontCommand());

        // Add the friends command
        commandHandler.registerCommand(new FriendsCommand(friendsManager));

        // Add Panic command
        commandHandler.registerCommand(new PanicCommand());

        commandHandler.registerCommand(new WaypointsCommand(
            ((Waypoints)Modules.get("waypoints")).waypoints
        ));

        // Add all of the modules as commands.
        for (Entry<String, Module> entry : Modules.entrySet()) {
            commandHandler.registerCommand(new ModuleCommand(entry.getKey(), entry.getValue()));
        }
    }

    // Modules
    public static HashMap<String, Module> Modules = new HashMap<String, Module>();

    // Chat
    public static void processChatPost(String message, CallbackInfo ci) {
        // Command Handling
        Integer commandHandlerOutput = commandHandler.handle(message);

        switch (commandHandlerOutput) {
            case -1:
                break;
            
            case 0: {
                // TODO have it display the command's help text.
                ComoClient.displayChatMessage(String.format("%sUnknown Command: Use 'help' for a list of commands.", ChatUtils.RED));
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
        ComoClient.log("Saving Client Config...");
        Persistance.saveConfig();

        ComoClient.log("It has been fun, remember to stay hydrated and that you matter <3");
    }

    public static void initialise() {
        ComoClient.log("Loading Como Client...");

        // TODO add persistance for the general config.
        // General config
        config = new GeneralConfig();

        // Setup the chat command system.
        commandHandler = new CommandHandler(isMeteorLoaded() ? config.alterativeCommandPrefix : config.commandPrefix);

        // Load up all the modules
        Modules.put("flight", new Flight());
        Modules.put("blink", new Blink());
        Modules.put("chatignore", new ChatIgnore());
        Modules.put("totemhide", new TotemHide());
        Modules.put("entityesp", new EntityESP());
        Modules.put("speed", new SpeedHack());
        Modules.put("superjump", new SuperJump());
        Modules.put("antiitemdrop", new AntiItemDrop());
        Modules.put("noweather", new NoWeather());
        Modules.put("nofall", new NoFall());
        Modules.put("camflight", new CamFlight());
        Modules.put("noboss", new NoBoss());
        Modules.put("elytraflight", new ElytraFlight());
        Modules.put("xray", new XRay());
        Modules.put("noenchantbook", new NoEnchantmentBook());
        Modules.put("nobreak", new NoBreak());
        Modules.put("autoshear", new AutoShear());
        Modules.put("tapemeasure", new TapeMeasure());
        Modules.put("modlist", new ModList());
        Modules.put("nohurtcam", new NoHurtCam());
        Modules.put("fullbright", new FullBright());
        Modules.put("autoreconnect", new AutoReconnect());
        Modules.put("autorespawn", new AutoRespawn());
        Modules.put("nofirecam", new NoFireCam());
        Modules.put("killaura", new KillAura());
        Modules.put("timer", new Timer());
        Modules.put("criticals", new Criticals());
        Modules.put("waypoints", new Waypoints());
        Modules.put("homegodmode", new HomeGodMode());
        Modules.put("itemrendertweaks", new ItemRenderTweaks());
        Modules.put("tracers", new Tracers());
        Modules.put("blockesp", new BlockESP());
        Modules.put("betternametags", new BetterNameTags());
        Modules.put("noportal", new NoPortal());
        Modules.put("shulkerpeak", new ShulkerPeak());
        Modules.put("nosubmerge", new NoSubmerge());
        Modules.put("watermark", new Watermark());
        Modules.put("freecam", new FreeCam());
        Modules.put("autototem", new AutoTotem());
        Modules.put("antiinvisible", new AntiInvisible());
        Modules.put("norespond", new NoRespondAlert());
        Modules.put("armourdisplay", new ArmourDisplay());
        Modules.put("crystalaura", new CrystalAura());
        Modules.put("xcarry", new XCarry());
        Modules.put("binds", new Binds());
        Modules.put("unfocuscpu", new UnfocusCPU());
        Modules.put("totempopcount", new TotemPopCount());
        Modules.put("shulkerdupe", new ShulkerDupe());
        Modules.put("discordrpc", new DiscordRichPres());
        Modules.put("hidetitlemessage", new HideTitleMessage());
        Modules.put("fastbreak", new FastBreak());
        Modules.put("entityspeed", new EntitySpeed());
        Modules.put("noeffect", new NoEffect());
        Modules.put("chatspam", new ChatSpam());
        Modules.put("infchat", new InfChat());
        Modules.put("autowalk", new AutoWalk());
        Modules.put("autosprint", new AutoSprint());
        Modules.put("entityspin", new EntitySpin());
        Modules.put("xstorage", new XStorage());
        Modules.put("instabowkill", new InstaBowKill());
        Modules.put("signsearch", new SignSearch());
        Modules.put("nuker", new Nuker());
        Modules.put("nocomcrash", new NoComCrash());
        Modules.put("packetlimiter", new PacketLimiter());
        Modules.put("craftingdupe", new CraftingDupe());
        Modules.put("hclip", new HClip());
        Modules.put("packetflight", new PacketFlight());
        Modules.put("fakeclient", new FakeClient());

        // TESTING DON'T GET EXCITED!
        Modules.put("clickgui", new ClickGUI());

        // Load the config (more module related stuff.)
        if (!Persistance.loadConfig()) {
            // It must be a new config.

            // Auto enable those that should be auto-enabled.
            for (String key : Modules.keySet()) {
                Module module = Modules.get(key);
                
                if (module.shouldAutoEnable()) module.enable();
            }
        }

        // Generate textRenderer
        updateFont(config.font);

        // Ready up all the commands
        registerModuleCommands();

        // Done!
        ComoClient.log("Como Client loaded!");
    }

    public static boolean isMeteorLoaded() {
        return FabricLoader.getInstance().isModLoaded("meteor-client");
    }

    ComoClient() { }
}
