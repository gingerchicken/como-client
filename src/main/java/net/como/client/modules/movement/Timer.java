package net.como.client.modules.movement;

import net.como.client.structures.Module;
import net.como.client.structures.settings.Setting;

public class Timer extends Module {
    public Timer() {
        super("Timer");

        this.addSetting(new Setting("Speed", 1d));

        this.description = "Change the client-side tick rate.";
    }
}
