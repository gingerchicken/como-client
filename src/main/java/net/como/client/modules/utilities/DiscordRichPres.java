package net.como.client.modules.utilities;

import java.util.HashMap;

import joptsimple.internal.Strings;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.como.client.ComoClient;
import net.como.client.events.BeginRenderTickEvent;
import net.como.client.events.OnClientCloseEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.network.ServerInfo;

// Screens
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public class DiscordRichPres extends Module {
    private final String APPLICATION_ID = "918490057546039407";
    private DiscordEventHandlers handlers;

    private String details, state;

    public DiscordRichPres() {
        super("DiscordRPC");

        this.description = "Displays which client you are using in discord rich presence.";

        this.addSetting(new Setting("ShowServer", false));
        this.setCategory("Utilities");
    }

    public String getState() {
        return this.state;
    }

    private Boolean isNewState(String state) {
        if (this.state == null) {
            return state != null;
        }

        return !this.state.equals(state);
    }

    private Boolean isNewDetails(String details) {
        if (this.details == null) {
            return details != null;
        }

        return !this.details.equals(details);
    }

    public void setState(String state) {
        if (state == null) return; // Use last state

        Boolean changed = this.isNewState(state);
        this.state = state;

        if (changed) this.createNewPresence();
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        Boolean changed = this.isNewDetails(details);
        this.details = details;

        if (changed) this.createNewPresence();
    }

    // Functions for setting specific information
    public DiscordRichPresence.Builder applyCurrentTime(DiscordRichPresence.Builder builder) {
        return builder.setStartTimestamps((long)ComoClient.getCurrentTime());
    }

    public DiscordRichPresence.Builder applyDetails(DiscordRichPresence.Builder builder) {
        return builder.setDetails(this.getDetails());
    }

    public DiscordRichPresence.Builder applyServer(DiscordRichPresence.Builder builder) {
        ServerInfo server = ServerUtils.getLastServer();

        if (server != null && ClientUtils.inGame() && !ComoClient.getClient().isInSingleplayer()) {
            builder = builder.setParty(this.getBoolSetting("ShowServer") ? server.address : "Multiplayer", ServerUtils.getTotalOnlinePlayers(), ServerUtils.getTotalPlayerSlots());
        }

        return builder;
    }

    public void createNewPresence() {
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(this.state);
        
        // Setup some information
        builder = applyCurrentTime(builder);
        builder = applyDetails(builder);
        builder = applyServer(builder);

        // Set the big image
        builder.setBigImage("icon", "Como Client");

        // Apply the new status
        DiscordRPC.discordUpdatePresence(builder.build());
    }

    public void initialiseDiscord() {
        handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            ComoClient.log("Welcome " + user.username + "#" + user.discriminator + "!");
        }).build();

        DiscordRPC.discordInitialize(APPLICATION_ID, handlers, true);
        this.createNewPresence();

        this.displayMessage("Connected to Discord!");
    }

    public void closeDiscord() {
        DiscordRPC.discordShutdown();

        this.state = this.details = Strings.EMPTY;

        this.displayMessage("Disconnected from Discord!");
    }

    @Override
    public void activate() {
        this.addListen(BeginRenderTickEvent.class);
        this.addListen(OnClientCloseEvent.class);

        this.initialiseDiscord();
    }

    @Override
    public void deactivate() {
        this.removeListen(BeginRenderTickEvent.class);
        this.removeListen(OnClientCloseEvent.class);

        this.closeDiscord();
    }

    // Welp since I cannot cuse switch statements.
    private HashMap<Class<? extends Screen>, String> screenStates = new HashMap<Class<? extends Screen>, String>() {
        {
            String stateMessage;
            this.put(TitleScreen.class, "Title Screen");
            
            stateMessage = "Looking for a multiplayer game";
            this.put(DirectConnectScreen.class, stateMessage);
            this.put(AddServerScreen.class, stateMessage);
            this.put(MultiplayerScreen.class, stateMessage);
        
            stateMessage = "Looking to play alone";
            this.put(CreateWorldScreen.class, stateMessage);
            this.put(SelectWorldScreen.class, stateMessage);
            
            this.put(ConfirmScreen.class, "Are you sure about that?");
            
            this.put(GameMenuScreen.class, "Paused");
        }
    };

    private String getScreenState() {
        Screen current = ComoClient.getClient().currentScreen;
        if (current == null) return null;

        // I cannot be bothered to type out like 50 things
        if (current instanceof OptionsScreen || current instanceof GameOptionsScreen) return "Tweaking Settings";
        
        return screenStates.containsKey(current.getClass()) ? screenStates.get(current.getClass()) : null;
    }

    private String getGameState() {
        if (ComoClient.getClient().isInSingleplayer()) {
            // TODO add the world name
            return "In Singleplayer";
        } 
        
        ServerInfo server = ServerUtils.getLastServer();
        if (server != null) {
            return "In Multiplayer";
        }

        return "Block Gaming";
    }

    private String getClientState() {
        // Handle if we are not in game.
        if (!ClientUtils.inGame()) {
            String screenState = this.getScreenState();
            if (screenState != null) return screenState;

            return null;
        }

        String state = this.getGameState();
        return state;
    }

    public String getGameDescription() {
        if (!ClientUtils.inGame()) return Strings.EMPTY;

        String state = ClientUtils.getGameModeName();
        ServerInfo server = ServerUtils.getLastServer();
        if (server != null && !ComoClient.getClient().isInSingleplayer()) {
            if (!this.getBoolSetting("ShowServer")) return state;

            return String.format("%s at %s", state, server.address);
        }


        return String.format("%s", ClientUtils.getGameModeName());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnClientCloseEvent": {
                this.closeDiscord();
                
                break;
            }

            case "BeginRenderTickEvent": {
                // I swear these should be the other way around but I think it looks slightly better this way.
                this.setState(this.getGameDescription());
                this.setDetails(this.getClientState());

                break;
            }
        }
    }
}
