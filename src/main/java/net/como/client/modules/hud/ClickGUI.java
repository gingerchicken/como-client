package net.como.client.modules.hud;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.events.ClientTickEvent;
import net.como.client.gui.impl.ClickGUIScreen;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ImGuiUtils;

public class ClickGUI extends Module {
    float scaleFactor = 1.5f;

    public ClickGUI() {
        super("ClickGUI");
        this.description = "A way of toggling your settings with a GUI (Currently WIP)";

        this.addSetting(new Setting("Bouncy", true) {{
            this.setDescription("Toggles the rendering of the bouncy felixes");
        }});
        
        this.addSetting(new Setting("TotalBouncies", 1) {{
            this.setDescription("The total amount of felixes bouncing around in the background!");
        }});

        this.addSetting(new Setting("BouncySpeed", 1.0d) {{
            this.setDescription("The speed of the felixes");

            this.setMax(50d);
            this.setMin(0d);
        }});

        this.addSetting(new Setting("Scale", 1.0d) {{
            this.setDescription("The scale of the GUI");
        }});

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
        commands.add(new ResetCommand());

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

    public class ResetCommand extends Command {

        public ResetCommand() {
            super("reset", "", "Resets the GUI window position");
        }

        @Override
        public Boolean trigger(String[] args) {
            ClickGUIScreen.resetNext = true;

            return true;
        }
    }
}
