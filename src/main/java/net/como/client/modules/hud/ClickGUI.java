package net.como.client.modules.hud;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.events.ClientTickEvent;
import net.como.client.gui.ClickGUIScreen;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.ImGuiUtils;

public class ClickGUI extends Module {
    float scaleFactor = 1.5f;

    public ClickGUI() {
        super("ClickGUI");
        this.description = "A way of toggling your settings with a GUI (Currently WIP)";

        this.setCategory("HUD");
    }

    ClickGUIScreen screen;

    @Override
    public void activate() {
        this.screen = new ClickGUIScreen();

        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        if (ComoClient.getClient().currentScreen instanceof ClickGUIScreen) {
            ComoClient.getClient().setScreen(null);
        }

        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        // TODO this is due to the fact that the activate command is called before the closeScreen function.
        // Basically, make it so that activate triggers after the chatscreen is closed.
        if (ComoClient.getClient().currentScreen == null) {
            ComoClient.getClient().setScreen(screen);
            
            ImGuiUtils.refreshStyle();
        }
    }

    @Override
    public Iterable<Command> getCommands() {
        List<Command> commands = new ArrayList<>();

        commands.add(new RefreshStyleCommand());

        return commands;
    }

    public class RefreshStyleCommand extends Command {

        public RefreshStyleCommand() {
            super("refresh", "", "Refreshes the GUI style");
        }

        @Override
        public Boolean trigger(String[] args) {
            ImGuiUtils.refreshStyle();

            return true;
        }
    }
}
