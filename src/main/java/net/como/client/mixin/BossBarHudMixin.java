package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.CheatClient;
import net.minecraft.client.gui.hud.BossBarHud;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(at = @At("HEAD"), method="render()V", cancellable = true)
    public void onRender(CallbackInfo ci) {
        CheatClient.triggerAllEvent("onBossBarHudRender", new Object[]{ci});
    }

    // Notice how these both have the same event trigger?  I mean... functionality wise, they are the same thing lets be honest.... figured rather than triggering different events for the same thing might as well keep it as one thing
    @Inject(at = @At("RETURN"), method="shouldDarkenSky()Z", cancellable = true)
    public void onShouldDarkenSky(CallbackInfoReturnable<Boolean> cir) {
        CheatClient.triggerAllEvent("onBossBarHudSkyEffects", new Object[]{cir});
    }

    @Inject(at = @At("RETURN"), method="shouldThickenFog()Z", cancellable = true)
    public void onShouldThickenFog(CallbackInfoReturnable<Boolean> cir) {
        CheatClient.triggerAllEvent("onBossBarHudSkyEffects", new Object[]{cir});
    }
}
