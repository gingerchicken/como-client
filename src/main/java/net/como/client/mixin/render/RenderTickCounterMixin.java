package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.ComoClient;
import net.como.client.events.client.BeginRenderTickEvent;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(RenderTickCounter.class)
public class RenderTickCounterMixin {
    @Inject(at = @At("HEAD"), method = "beginRenderTick(J)I", cancellable = true)
    private void onBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> ci) {
        ComoClient.emitter.triggerEvent(new BeginRenderTickEvent(timeMillis, ci));
    }
}
