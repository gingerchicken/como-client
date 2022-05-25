package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.render.RenderFireOverlayEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRenderMixin {
    @Inject(at = @At("HEAD"), method = "renderFireOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", cancellable = true)
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new RenderFireOverlayEvent(client, matrices, ci));
    }
}
