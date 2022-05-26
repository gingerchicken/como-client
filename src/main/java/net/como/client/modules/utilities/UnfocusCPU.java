package net.como.client.modules.utilities;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.minecraft.client.util.Window;

public class UnfocusCPU extends Module {

    public UnfocusCPU() {
        super("UnfocusCPU", true);

        this.setDescription("Decreases game performance while the window is not focused.");

        this.addSetting(new Setting("MaxFPS", 15));
        this.setCategory("Utilities");
    }
    
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                Window window = ComoClient.getClient().getWindow();
                if (!ComoClient.getClient().isWindowFocused()) {
                    window.setFramerateLimit(this.getIntSetting("MaxFPS"));
                } else {
                    window.setFramerateLimit(ComoClient.getClient().options.maxFps);
                }
            }
        }
    }
}
