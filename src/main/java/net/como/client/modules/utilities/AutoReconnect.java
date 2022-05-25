package net.como.client.modules.utilities;

import net.como.client.ComoClient;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.gui.screen.Screen;

public class AutoReconnect extends Module {
    public AutoReconnect() {
        super("AutoReconnect");

        this.addSetting(new Setting("Delay", 5));
        this.addSetting(new Setting("Manual", false));
        this.addSetting(new Setting("InGameButton", true));

        this.setDescription("Automatically reconnects to a server after a given time.");
        this.setCategory("Utilities");
    }

    private double startTime = 0;

    public void reconnect(Screen prevScreen) {
       ServerUtils.connectToServer(ServerUtils.getLastServer(), prevScreen);
    }
    public void startCountdown() {
        this.startTime = ComoClient.getCurrentTime();
    }
    
    public double workCountdown(Screen prevScreen) {
        double delta = this.deltaTime();
        double delay = Double.valueOf((int)this.getSetting("Delay").value);

        double timeRemaining = delay - delta;

        if (timeRemaining <= 0d) {
            this.reconnect(prevScreen);
            return 0d;
        }

        return timeRemaining;
    }
    private double deltaTime() {
        return ComoClient.getCurrentTime() - startTime;
    }
}
