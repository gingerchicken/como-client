package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.gui.screen.Screen;

public class AutoReconnect extends Cheat {
    public AutoReconnect() {
        super("AutoReconnect");

        this.addSetting(new Setting("Delay", 5));
        this.addSetting(new Setting("Manual", false));
    }

    private double startTime = 0;

    public void reconnect(Screen prevScreen) {
       ServerUtils.connectToServer(ServerUtils.getLastServer(), prevScreen);
    }
    private double deltaTime() {
        return CheatClient.getCurrentTime() - startTime;
    };
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

    public void receiveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onDisconnect": {
                // Start timer if manual mode is not enabled.
                if ((boolean)this.getSetting("Manual").value) return;
                this.startCountdown();

                break;
            }
        }
    }
}
