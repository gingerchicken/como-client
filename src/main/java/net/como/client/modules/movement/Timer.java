package net.como.client.modules.movement;

import net.como.client.config.settings.Setting;
import net.como.client.modules.DummyModule;

public class Timer extends DummyModule {
    public Timer() {
        super("Timer");

        this.addSetting(new Setting("Speed", 1d));

        this.setDescription("Change the client-side tick rate.");

        this.setCategory("Movement");
    }
}
