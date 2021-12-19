package net.como.client.modules;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak");

        this.description = "Allows you to break blocks a bit quicker.";

        this.addSetting(new Setting("Amplifier", 3));
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);

        if (ComoClient.me().hasStatusEffect(StatusEffects.HASTE)) {
            ComoClient.me().removeStatusEffect(StatusEffects.HASTE);
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ComoClient.me().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 3, this.getIntSetting("Amplifier"), true, true));
                
                break;
            }
        }
    }
}
