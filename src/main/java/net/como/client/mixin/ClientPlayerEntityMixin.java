package net.como.client.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("HEAD"), method="sendChatMessage(Ljava/lang/String;)V", cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo callbackInfo) {
        CheatClient.triggerAllEvent("onPlayerChat", new Object[]{message, callbackInfo});
    }

    @Inject(at = @At("HEAD"), method="sendMovementPackets()V", cancellable = true)
    private void onSendMovementPackets(CallbackInfo callbackInfo) {
        CheatClient.triggerAllEvent("onMovementPacket", new Object[]{callbackInfo});
    }

    @Inject(at = @At("RETURN"), method="tick()V")
    private void onTick(CallbackInfo callbackInfo) {
        CheatClient.triggerAllEvent("onClientTick", new Object[]{callbackInfo});
    }
}