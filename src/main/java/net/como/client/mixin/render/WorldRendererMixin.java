package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.render.RenderEntityEvent;
import net.como.client.events.render.RenderWorldEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method="renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    public void renderEntity(net.minecraft.entity.Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack mStack, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new RenderEntityEvent(entity, cameraX, cameraY, cameraZ, tickDelta, mStack, vertexConsumers, ci));
    }

    @Inject(at = @At("RETURN"), method="render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", cancellable = true)
    public void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager arg4, Matrix4f arg5, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new RenderWorldEvent(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, arg4, arg5, ci));
    }
}
