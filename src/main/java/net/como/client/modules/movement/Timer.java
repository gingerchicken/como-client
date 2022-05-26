package net.como.client.modules.movement;

import net.como.client.misc.settings.Setting;
import net.como.client.modules.Module;

public class Timer extends Module {
    public Timer() {
        super("Timer");

        this.addSetting(new Setting("Speed", 1d));

        this.setDescription("Change the client-side tick rate.");

        this.setCategory("Movement");
    }
}
