package net.como.client.modules.movement;

import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;

public class Timer extends Module {
    public Timer() {
        super("Timer");

        this.addSetting(new Setting("Speed", 1d));

        this.setDescription("Change the client-side tick rate.");

        this.setCategory("Movement");
    }
}
