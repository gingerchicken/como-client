package net.como.client.modules;

import java.util.Collection;
import java.util.HashMap;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;

public class NoEffect extends Module {
    public NoEffect() {
        super("NoEffect");

        this.description = "Allows you to disable specific effects.";

        this.addSetting(new Setting("Effects", new HashMap<String, Boolean>()));
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                Collection<StatusEffectInstance> effects =  ComoClient.me().getStatusEffects();
                
                HashMap<String, Boolean> cancelEffects = this.getHashMapSetting("Effects");
                for (StatusEffectInstance effect : effects) {
                    String parts[] = effect.getTranslationKey().split("\\.");
                    if (parts.length == 0) continue;
                    
                    String name = parts[parts.length - 1];
                    if (cancelEffects.containsKey(name)) ComoClient.me().removeStatusEffect(effect.getEffectType());
                }

                break;
            }
        }
    }
}
