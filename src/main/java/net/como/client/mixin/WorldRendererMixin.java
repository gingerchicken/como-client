package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method="renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    public void renderEntity(net.minecraft.entity.Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderEntity", new Object[]{
            entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers, ci
        });
    }

    @Inject(at = @At("RETURN"), method="render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", cancellable = true)
    public void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager arg4, Matrix4f arg5, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderWorld", new Object[]{
            matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, arg4, arg5, ci
        });
    }

    @Inject(at = @At("HEAD"), method="renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V", cancellable = true)
    private void onRenderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderWeather", new Object[] {
            manager, f, d, e, g, ci
        });
    }
    
    @Inject(at = @At("HEAD"), method="tickRainSplashing(Lnet/minecraft/client/render/Camera;)V", cancellable = true)
    private void onTickRainSplashing(Camera camera, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onTickRainSplashing", new Object[] {
            camera, ci
        });
    }

    @Inject(at = @At("HEAD"), method="renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FDDD)V", cancellable = true)
    private void onRenderClouds(MatrixStack matrices, Matrix4f m4f, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderClouds", new Object[] {
            matrices, m4f, tickDelta, cameraX, cameraY, cameraZ, ci
        });
    }

    @Inject(at = @At("HEAD"), method="renderDarkSky()V", cancellable = true)
    private void onRenderDarkSky(CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderDarkSky", new Object[] {
            ci
        });
    }
}
