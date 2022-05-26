package net.como.client.modules.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.settings.Setting;
import net.como.client.modules.Module;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public class NoEffect extends Module {
    public NoEffect() {
        super("NoEffect");

        this.setDescription("Allows you to disable specific effects.");

        this.addSetting(new Setting("Effects", new HashMap<String, Boolean>()));

        this.setCategory("Render");
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
                
                List<StatusEffect> targets = new ArrayList<>();
                HashMap<String, Boolean> cancelEffects = this.getHashMapSetting("Effects");
                for (StatusEffectInstance effect : effects) {
                    String parts[] = effect.getTranslationKey().split("\\.");
                    if (parts.length == 0) continue;
                    
                    String name = parts[parts.length - 1];
                    if (cancelEffects.containsKey(name)) targets.add(effect.getEffectType());
                }

                // Remove them
                for (StatusEffect effect : targets) {
                    ComoClient.me().removeStatusEffect(effect);
                }

                break;
            }
        }
    }
}
