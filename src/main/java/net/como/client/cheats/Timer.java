package net.como.client.cheats;

import net.como.client.structures.Cheat;
import net.como.client.structures.settings.Setting;

public class Timer extends Cheat {
    public Timer() {
        super("Timer");

        this.addSetting(new Setting("Speed", 1d));
    }
}
