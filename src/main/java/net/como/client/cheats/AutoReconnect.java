package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.DisconnectEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.gui.screen.Screen;

public class AutoReconnect extends Cheat {
    public AutoReconnect() {
        super("AutoReconnect");

        this.addSetting(new Setting("Delay", 5));
        this.addSetting(new Setting("Manual", false));

        this.description = "Automatically reconnects to a server after a given time";
    }

    private double startTime = 0;

    public void reconnect(Screen prevScreen) {
       ServerUtils.connectToServer(ServerUtils.getLastServer(), prevScreen);
    }
    public void startCountdown() {
        this.startTime = CheatClient.getCurrentTime();
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
        return CheatClient.getCurrentTime() - startTime;
    }
}
