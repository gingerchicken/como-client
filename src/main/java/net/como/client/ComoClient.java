package net.como.client;


import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.commands.CopyServerIPCommand;
import net.como.client.commands.FontCommand;
import net.como.client.commands.FriendsCommand;
import net.como.client.commands.PanicCommand;
import net.como.client.commands.WaypointsCommand;
import net.como.client.commands.nbt.GiveCommand;
import net.como.client.commands.nbt.NbtCommand;
import net.como.client.commands.structures.ModuleCommand;
import net.como.client.commands.structures.CommandHandler;
import net.como.client.components.FriendsManager;
import net.como.client.events.EventEmitter;
import net.como.client.misc.Module;
import net.como.client.modules.chat.*;
import net.como.client.modules.combat.*;
import net.como.client.modules.dupes.*;
import net.como.client.modules.exploits.*;
import net.como.client.modules.hud.ArmourDisplay;
import net.como.client.modules.hud.ClickGUI;
import net.como.client.modules.hud.Hitmarker;
import net.como.client.modules.hud.MinifiedHealth;
import net.como.client.modules.hud.ModList;
import net.como.client.modules.hud.Watermark;
import net.como.client.modules.movement.*;
import net.como.client.modules.packet.*;
import net.como.client.modules.render.*;
import net.como.client.modules.utilities.*;
import net.como.client.utils.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ComoClient {
    private static ComoClient instance = null;
    
    /**
     * Get's the singleton instance of Como Client
     */
    public static ComoClient getInstance() {
        if (instance == null) {
            instance = new ComoClient();
        }

        return instance;
    }

    // Variables
    private final static String CHAT_PREFIX = ChatUtils.chatPrefix("Como Client");

    private static final Logger LOGGER = LogManager.getLogger("Como Client");

    public CommandHandler commandHandler;
    public EventEmitter emitter = new EventEmitter();
    public FriendsManager friendsManager = new FriendsManager();
    public TextRenderer textRenderer;
    public GeneralConfig config;

    /**
     * Log an object's value to the console
     * @param obj The object to log
     */
    public static void log(Object obj) {
        log(obj.toString());
    }

    /**
     * Log a string to the console
     * @param str The string to log
     */
    public static void log(String str) {
        // I don't want no rats
        str = str.replaceAll("jndi:ldap", "sug:ma");

        LOGGER.info(str);
    }

    /**
     * Update Como Client's default font to a given Minecraft font ID
     * @param fontId The font's identifier
     */
    public void updateFont(Identifier fontId) {
        this.textRenderer = FontUtils.createTextRenderer(fontId);
    }

    /**
     * Update Como Client's default font to a given minecraft font ID
     * @param id The Minecraft font ID
     */
    public void updateFont(String id) {
        this.updateFont(new Identifier(id));
    }

    // Commands

    /**
     * Register all of the module's commands
     */
    private void registerModuleCommands() {
        // Add the font command
        commandHandler.registerCommand(new FontCommand());

        // Add the friends command
        commandHandler.registerCommand(new FriendsCommand(friendsManager));

        // Add Panic command
        commandHandler.registerCommand(new PanicCommand());

        // Add copyip command
        commandHandler.registerCommand(new CopyServerIPCommand());

        // Add nbt command
        commandHandler.registerCommand(new NbtCommand());

        // Waypoints commands
        commandHandler.registerCommand(new WaypointsCommand(
            ((Waypoints)modules.get("waypoints")).waypoints
        ));

        // Add give command
        commandHandler.registerCommand(new GiveCommand());

        // Add all of the modules as commands.
        for (Entry<String, Module> entry : modules.entrySet()) {
            commandHandler.registerCommand(new ModuleCommand(entry.getKey(), entry.getValue()));
        }
    }

    // modules
    private HashMap<String, Module> modules = new HashMap<String, Module>();

    public HashMap<String, Module> getModules() {
        return modules;
    }

    // Chat
    public void processChatPost(String message, CallbackInfo ci) {
        // Command Handling
        Integer commandHandlerOutput = commandHandler.handle(message);

        switch (commandHandlerOutput) {
            case -1:
                break;
            
            case 0: {
                // TODO have it display the command's help text.
                this.displayChatMessage(String.format("%sUnknown Command: Use 'help' for a list of commands.", ChatUtils.RED));
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

    /**
     * Get's the Minecraft Client instance
     * @return The Minecraft Client instance
     */
    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    /**
     * Get's the Client Player Entity
     * @return The Client Player Entity
     */
    public static ClientPlayerEntity me() {
        // Get the client
        MinecraftClient client = getClient();

        // Return localplayer
        return client.player;
    }

    /**
     * Get the time in milliseconds
     * @return The time in milliseconds
     */
    public static double getCurrentTime() {
        return Double.valueOf(System.currentTimeMillis()) / 1000d; // Seconds
    }

    public void close() {
        log("Saving Client Config...");
        Persistance.saveConfig();

        log("It has been fun, remember to stay hydrated and that you matter <3");
    }

    public void initialise() {
        log("Loading Como Client...");

        // TODO add persistance for the general config.
        // General config
        config = new GeneralConfig();

        // Setup the chat command system.
        commandHandler = new CommandHandler(isMeteorLoaded() ? config.alterativeCommandPrefix : config.commandPrefix);

        // Load up all the modules
        modules.put("flight", new Flight());
        modules.put("blink", new Blink());
        modules.put("chatignore", new ChatIgnore());
        modules.put("totemhide", new TotemHide());
        modules.put("entityesp", new EntityESP());
        modules.put("speed", new SpeedHack());
        modules.put("superjump", new SuperJump());
        modules.put("antiitemdrop", new NoItemRender());
        modules.put("noweather", new NoWeather());
        modules.put("nofall", new NoFall());
        modules.put("camflight", new CamFlight());
        modules.put("noboss", new NoBoss());
        modules.put("elytraflight", new ElytraFlight());
        modules.put("xray", new XRay());
        modules.put("noenchantbook", new NoEnchantmentBook());
        modules.put("nobreak", new NoBreak());
        modules.put("autoshear", new AutoShear());
        modules.put("tapemeasure", new TapeMeasure());
        modules.put("modlist", new ModList());
        modules.put("nohurtcam", new NoHurtCam());
        modules.put("fullbright", new FullBright());
        modules.put("autoreconnect", new AutoReconnect());
        modules.put("autorespawn", new AutoRespawn());
        modules.put("nofirecam", new NoFireCam());
        modules.put("killaura", new KillAura());
        modules.put("timer", new Timer());
        modules.put("criticals", new Criticals());
        modules.put("waypoints", new Waypoints());
        modules.put("homegodmode", new HomeGodMode());
        modules.put("itemrendertweaks", new ItemRenderTweaks());
        modules.put("tracers", new Tracers());
        modules.put("blockesp", new BlockESP());
        modules.put("betternametags", new BetterNameTags());
        modules.put("noportal", new NoPortal());
        modules.put("shulkerpeak", new ShulkerPeak());
        modules.put("nosubmerge", new NoSubmerge());
        modules.put("watermark", new Watermark());
        modules.put("freecam", new FreeCam());
        modules.put("autototem", new AutoTotem());
        modules.put("antiinvisible", new AntiInvisible());
        modules.put("norespond", new NoRespondAlert());
        modules.put("armourdisplay", new ArmourDisplay());
        modules.put("crystalaura", new CrystalAura());
        modules.put("xcarry", new XCarry());
        modules.put("binds", new Binds());
        modules.put("unfocuscpu", new UnfocusCPU());
        modules.put("totempopcount", new TotemPopCount());
        modules.put("shulkerdupe", new ShulkerDupe());
        modules.put("discordrpc", new DiscordRichPres());
        modules.put("hidetitlemessage", new HideTitleMessage());
        modules.put("fastbreak", new FastBreak());
        modules.put("entityspeed", new EntitySpeed());
        modules.put("noeffect", new NoEffect());
        modules.put("chatspam", new ChatSpam());
        modules.put("infchat", new InfChat());
        modules.put("autowalk", new AutoWalk());
        modules.put("autosprint", new AutoSprint());
        modules.put("entityspin", new EntitySpin());
        modules.put("xstorage", new XStorage());
        modules.put("instabowkill", new InstaBowKill());
        modules.put("signsearch", new SignSearch());
        modules.put("nuker", new Nuker());
        modules.put("nocomcrash", new NoComCrash());
        modules.put("packetlimiter", new PacketLimiter());
        modules.put("craftingdupe", new CraftingDupe());
        modules.put("hclip", new HClip());
        modules.put("packetflight", new PacketFlight());
        modules.put("fakeclient", new FakeClient());
        modules.put("hitmarker", new Hitmarker());
        modules.put("antiresourcepack", new AntiResourcePack());
        modules.put("antikick", new AntiKick());
        modules.put("fastuse", new FastUse());
        modules.put("commandautofill", new CommandAutoFill());
        modules.put("creativemagic", new CreativeMagic());
        modules.put("noparticles", new NoParticles());
        modules.put("packetsniffer", new PacketSniffer());
        modules.put("noslow", new NoSlow());
        modules.put("fastattack", new FastAttack());
        modules.put("femboymod", new FemboyMod());
        modules.put("offhandcrash", new OffHandCrash());
        modules.put("entityowner", new EntityOwner());
        modules.put("lecterncrash", new LecternCrash());
        modules.put("minifiedhealth", new MinifiedHealth());
        modules.put("quakeaimbot", new QuakeAimbot());
        modules.put("x88esp", new x88ESP());
        modules.put("mapartesp", new MapArtESP());

        // TESTING DON'T GET EXCITED!
        modules.put("clickgui", new ClickGUI());

        // Load the config (more module related stuff.)
        if (!Persistance.loadConfig()) {
            // It must be a new config.

            // Auto enable those that should be auto-enabled.
            for (String key : modules.keySet()) {
                Module module = modules.get(key);
                
                if (module.shouldAutoEnable()) module.enable();
            }
        }

        // Load all of the block ids
        BlockUtils.initialiseIdList();

        // Generate textRenderer
        updateFont(config.font);

        // Ready up all the commands
        registerModuleCommands();

        // Done!
        log("Como Client loaded!");
    }

    public static boolean isMeteorLoaded() {
        return FabricLoader.getInstance().isModLoaded("meteor-client");
    }
}
