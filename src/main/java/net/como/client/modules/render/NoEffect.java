package net.como.client.modules.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.modules.Module;
import net.como.client.utils.ClientUtils;
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
                    String name = ClientUtils.getTextString(effect.getEffectType().getName());
                    if (cancelEffects.containsKey(name)) {
                        targets.add(effect.getEffectType());
                    }
                }

                // Remove them
                for (StatusEffect effect : targets) {
                    ComoClient.me().removeStatusEffect(effect);
                }

                break;
            }
        }
    }

    @Override
    public Iterable<Command> getCommands() {
        List<Command> commands = new ArrayList<>();

        commands.add(new ListEffectsCommand());

        return commands;
    }

    private static class ListEffectsCommand extends Command {

        public NoEffect getNoEffect() {
            return (NoEffect) ComoClient.getInstance().getModules().get("noeffect");
        }

        public ListEffectsCommand() {
            super("list", "", "lists all active effects");
        }
        
        @Override
        public Boolean trigger(String[] args) {
            if (ComoClient.me().getStatusEffects().isEmpty()) {
                this.getNoEffect().displayMessage("There are no active effects.");

                return true;
            }

            this.getNoEffect().displayMessage("Active effects:");
            for (StatusEffectInstance effect : ComoClient.me().getStatusEffects()) {
                String name = ClientUtils.getTextString(effect.getEffectType().getName());
                this.getNoEffect().displayMessage("-> " + name);
            }

            return true;
        }
    }
}
