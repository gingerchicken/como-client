package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.Mode;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {
    private Double normalGamma = 0d;
    Boolean hasSetGamma = false;

    public FullBright() {
        super("FullBright");
        
        this.setDescription("Allows you to see anywhere as if it was day.");

        this.addSetting(new Setting("Mode", new Mode("Gamma", "Potion")));

        this.setCategory("Render");
    }

    @Override
    public String listOption() {
        return this.getModeSetting("Mode").getStateName();
    }
    
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    private void restoreGamma() {
        if (!this.hasSetGamma) return;

        MinecraftClient client = ComoClient.getClient();
        client.options.gamma = this.normalGamma;
        this.hasSetGamma = false;
    }

    private void restoreEffect() {
        if (ComoClient.me().hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            ComoClient.me().removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);

        this.restoreGamma();
        this.restoreEffect();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                switch (this.getModeSetting("Mode").getStateName()) {
                    case "Potion": {
                        // Restore other mod's gamma
                        this.restoreGamma();

                        ComoClient.me().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3, 1, true, true));

                        break;
                    }

                    case "Gamma": {
                        MinecraftClient client = ComoClient.getClient();

                        if (!this.hasSetGamma) {
                            this.normalGamma = client.options.gamma;
                            this.hasSetGamma = true;
        
                            this.restoreEffect();
                        }
        
                        client.options.gamma = 16d;
                        break;
                    }
                }
            }
        }
    }
}
