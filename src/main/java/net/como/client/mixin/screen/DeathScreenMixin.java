package net.como.client.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.screen.DeathEvent;
import net.minecraft.client.gui.screen.DeathScreen;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Inject(at = @At("TAIL"), method = "init()V", cancellable = false)
    public void init(CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new DeathEvent(ci));
    }
}
