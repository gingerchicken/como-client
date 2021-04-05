package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method="renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    public void renderEntity(net.minecraft.entity.Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderEntity", new Object[]{
            entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers, ci
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

    @Inject(at = @At("HEAD"), method="renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V", cancellable = true)
    private void onRenderClouds(MatrixStack matrices, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderClouds", new Object[] {
            matrices, tickDelta, cameraX, cameraY, cameraZ, ci
        });
    }

    @Inject(at = @At("HEAD"), method="renderDarkSky()V", cancellable = true)
    private void onRenderDarkSky(CallbackInfo ci) {
        CheatClient.triggerAllEvent("onRenderDarkSky", new Object[] {
            ci
        });
    }
}
