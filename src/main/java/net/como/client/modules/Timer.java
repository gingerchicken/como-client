package net.como.client.modules;

import net.como.client.structures.Cheat;
import net.como.client.structures.settings.Setting;

public class Timer extends Cheat {
    public Timer() {
        super("Timer");

        this.addSetting(new Setting("Speed", 1d));

        this.description = "Change the client-side tick rate.";
    }
}
