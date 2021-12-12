package net.como.client.modules;

import joptsimple.internal.Strings;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.como.client.ComoClient;
import net.como.client.events.BeginRenderTickEvent;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.network.ServerInfo;

public class DiscordRichPres extends Module {
    private final String APPLICATION_ID = "918490057546039407";
    private DiscordEventHandlers handlers;

    private String details, state;

    public DiscordRichPres() {
        super("DiscordRPC");

        this.description = "Displays which client you are using in discord rich presence.";

        this.addSetting(new Setting("ShowServer", false));
    }

    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        if (state == null) return;
        if (state.equals(Strings.EMPTY)) state = null;

        Boolean changed = this.state == null || !this.state.equals(state);
        this.state = state;

        if (changed) this.createNewPresence();
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        Boolean changed = this.details == null || !this.details.equals(details);
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
    }

    public void closeDiscord() {
        DiscordRPC.discordShutdown();

        this.state = this.details = Strings.EMPTY;
    }

    @Override
    public void activate() {
        this.addListen(BeginRenderTickEvent.class);

        this.initialiseDiscord();
    }

    @Override
    public void deactivate() {
        this.removeListen(BeginRenderTickEvent.class);

        this.closeDiscord();
    }

    private String getScreenState() {
        Screen current = ComoClient.getClient().currentScreen;
        if (current == null) return null;

        // I cannot be bothered to type out like 50 things
        if (current instanceof OptionsScreen || current instanceof GameOptionsScreen) return "Tweaking Settings";

        switch (current.getClass().getSimpleName()) {
            case "TitleScreen": return "Title Screen";

            case "DirectConnectScreen":
            case "AddServerScreen":
            case "MultiplayerScreen": return "Looking for a multiplayer game";

            case "CreateWorldScreen":
            case "SelectWorldScreen": return "Looking to play alone";

            case "GameMenuScreen": return "Paused";

            case "ConfirmScreen": return "Are you sure about that?";
        }

        return null;
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

        String state = ClientUtils.getGamemode();
        ServerInfo server = ServerUtils.getLastServer();
        if (server != null && !ComoClient.getClient().isInSingleplayer()) {
            if (!this.getBoolSetting("ShowServer")) return state;

            return String.format("%s at %s", state, server.address);
        }


        return String.format("%s", ClientUtils.getGamemode());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "BeginRenderTickEvent": {
                // I swear these should be the other way around but I think it looks slightly better this way.
                this.setState(this.getGameDescription());
                this.setDetails(this.getClientState());

                break;
            }
        }
    }
}
