package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.events.OnClientCloseEvent;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "close()V", cancellable = false)
    private void onClose(CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new OnClientCloseEvent(ci));
    }
}
