package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {
    private Double normalGamma = 0d;
    Boolean hasSetGamma = false;

    public FullBright() {
        super("FullBright");
        
        this.description = "Allows you to see anywhere as if it was day.";

        // TODO change this once you add EnumSettings
        this.addSetting(new Setting("PotionEffect", false));

        this.setCategory("Render");
    }

    @Override
    public String listOption() {
        return this.getBoolSetting("PotionEffect") ? "Potion" : "Gamma";
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
                if (this.getBoolSetting("PotionEffect")) {
                    // Restore other mod's gamma
                    this.restoreGamma();

                    ComoClient.me().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3, 1, true, true));

                    break;
                }

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