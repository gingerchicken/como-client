package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.minecraft.client.gui.screen.DeathScreen;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Inject(at = @At("TAIL"), method = "init()V", cancellable = false)
    public void init(CallbackInfo ci) {
        CheatClient.triggerAllEvent("onDeath", new Object[] {
            ci
        });
    }
}
