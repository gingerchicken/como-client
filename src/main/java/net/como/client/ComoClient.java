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
import net.como.client.commands.exploits.DiscardLocal;
import net.como.client.commands.exploits.ParticleCrash;
import net.como.client.commands.nbt.GiveCommand;
import net.como.client.commands.nbt.ItemEggCommand;
import net.como.client.commands.nbt.NbtCommand;
import net.como.client.commands.structures.ModuleCommand;
import net.como.client.components.systems.FriendsManager;
import net.como.client.commands.structures.CommandHandler;
import net.como.client.config.GeneralConfig;
import net.como.client.config.Persistence;
import net.como.client.events.EventEmitter;
import net.como.client.modules.Module;
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
     * Register commands
     */
    private void registerCommands() {
        // Add the font command
        this.commandHandler.registerCommand(new FontCommand());

        // Add the friends command
        this.commandHandler.registerCommand(new FriendsCommand(this.friendsManager));

        // Add Panic command
        this.commandHandler.registerCommand(new PanicCommand());

        // Add copyip command
        this.commandHandler.registerCommand(new CopyServerIPCommand());

        // Add nbt commands
        this.commandHandler.registerCommand(new NbtCommand());
        this.commandHandler.registerCommand(new GiveCommand());
        this.commandHandler.registerCommand(new ItemEggCommand());

        // Exploit commands
        this.commandHandler.registerCommand(new ParticleCrash());
        this.commandHandler.registerCommand(new DiscardLocal());

        // TODO replace this with just a Module sub command
        // Waypoints commands
        commandHandler.registerCommand(new WaypointsCommand(
            ((Waypoints)modules.get("waypoints")).waypoints
        ));
    }

    /**
     * Register all of the module's commands
     */
    private void registerModuleCommands() {
        // Add all of the modules as commands.
        for (Entry<String, Module> entry : modules.entrySet()) {
            commandHandler.registerCommand(new ModuleCommand(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Module storage
     */
    private HashMap<String, Module> modules = new HashMap<String, Module>();

    /**
     * Gets the modules storage
     * @return The modules storage
     */
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
                displayChatMessage(String.format("%sUnknown Command: Use 'help' for a list of commands.", ChatUtils.RED));
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
        Persistence.saveConfig();

        log("It has been fun, remember to stay hydrated and that you matter <3");
    }

    /**
     * Registers all of the given modules
     */
    private void registerModules() {
        // Load up all the modules
        this.registerModule(new Flight());
        this.registerModule(new Blink());
        this.registerModule(new ChatIgnore());
        this.registerModule(new TotemHide());
        this.registerModule(new EntityESP());
        this.registerModule(new SpeedHack());
        this.registerModule(new SuperJump());
        this.registerModule(new NoItemRender());
        this.registerModule(new NoWeather());
        this.registerModule(new NoFall());
        this.registerModule(new CamFlight());
        this.registerModule(new NoBoss());
        this.registerModule(new ElytraFlight());
        this.registerModule(new XRay());
        this.registerModule(new NoEnchantmentBook());
        this.registerModule(new NoBreak());
        this.registerModule(new AutoShear());
        this.registerModule(new TapeMeasure());
        this.registerModule(new ModList());
        this.registerModule(new NoHurtCam());
        this.registerModule(new FullBright());
        this.registerModule(new AutoReconnect());
        this.registerModule(new AutoRespawn());
        this.registerModule(new NoFireCam());
        this.registerModule(new KillAura());
        this.registerModule(new Timer());
        this.registerModule(new Criticals());
        this.registerModule(new Waypoints());
        this.registerModule(new HomeGodMode());
        this.registerModule(new ItemRenderTweaks());
        this.registerModule(new Tracers());
        this.registerModule(new BlockESP());
        this.registerModule(new BetterNameTags());
        this.registerModule(new NoPortal());
        this.registerModule(new ShulkerPeak());
        this.registerModule(new NoSubmerge());
        this.registerModule(new Watermark());
        this.registerModule(new FreeCam());
        this.registerModule(new AutoTotem());
        this.registerModule(new AntiInvisible());
        this.registerModule(new NoRespondAlert());
        this.registerModule(new ArmourDisplay());
        this.registerModule(new CrystalAura());
        this.registerModule(new XCarry());
        this.registerModule(new Binds());
        this.registerModule(new UnfocusCPU());
        this.registerModule(new TotemPopCount());
        this.registerModule(new ShulkerDupe());
        this.registerModule(new DiscordRichPres());
        this.registerModule(new HideTitleMessage());
        this.registerModule(new FastBreak());
        this.registerModule(new EntitySpeed());
        this.registerModule(new NoEffect());
        this.registerModule(new ChatSpam());
        this.registerModule(new InfChat());
        this.registerModule(new AutoWalk());
        this.registerModule(new AutoSprint());
        this.registerModule(new EntitySpin());
        this.registerModule(new XStorage());
        this.registerModule(new InstaBowKill());
        this.registerModule(new SignSearch());
        this.registerModule(new Nuker());
        this.registerModule(new NoComCrash());
        this.registerModule(new PacketLimiter());
        this.registerModule(new CraftingDupe());
        this.registerModule(new HClip());
        this.registerModule(new PacketFlight());
        this.registerModule(new FakeClient());
        this.registerModule(new Hitmarker());
        this.registerModule(new AntiResourcePack());
        this.registerModule(new AntiKick());
        this.registerModule(new FastUse());
        this.registerModule(new CommandAutoFill());
        this.registerModule(new CreativeMagic());
        this.registerModule(new NoParticles());
        this.registerModule(new PacketSniffer());
        this.registerModule(new NoSlow());
        this.registerModule(new FastAttack());
        this.registerModule(new FemboyMod());
        this.registerModule(new OffHandCrash());
        this.registerModule(new EntityOwner());
        this.registerModule(new LecternCrash());
        this.registerModule(new MinifiedHealth());
        this.registerModule(new QuakeAimbot());
        this.registerModule(new x88ESP());
        this.registerModule(new MapArtESP());
        this.registerModule(new NoEntityRender());
        this.registerModule(new ExploitSkid());
        this.registerModule(new Placer());

        this.registerModule(new ClickGUI());
    }

    private void loadPersistance() {
        if (Persistence.loadConfig()) return;

        // It must be a new config.

        // Auto enable those that should be auto-enabled.
        for (String key : modules.keySet()) {
            Module module = modules.get(key);
            
            if (module.shouldAutoEnable()) module.enable();
        }
    }

    public void initialise() {
        log("Loading Como Client...");

        // TODO add persistance for the general config.
        // General config
        this.config = new GeneralConfig();

        // Setup the chat command system
        this.commandHandler = new CommandHandler(ThirdPartyUtils.isOtherClientLoaded() ? config.alterativeCommandPrefix : config.commandPrefix);

        this.registerModules();

        // Load the persistence
        this.loadPersistance();

        // Register commands
        this.registerCommands();
        this.registerModuleCommands();

        // Load all of the block ids
        BlockUtils.initialiseIdList();

        // Generate textRenderer
        this.updateFont(config.font);

        // Done!
        log("Como Client loaded!");
    }

    /**
     * Registers a given module
     * @param module The module to register
     * @return if the module was registered
     */
    public boolean registerModule(Module module) {
        String key = module.getName().toLowerCase();

        // Check if the module is already registered.
        if (modules.containsKey(key)) {
            log("Module " + key + " is already registered!");
            return false;
        }

        // Add the module
        this.modules.put(key, module);

        return true;
    }
}
