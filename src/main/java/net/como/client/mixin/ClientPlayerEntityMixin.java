package net.como.client.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.MovementPacketEvent;
import net.como.client.events.PlayerChatEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("HEAD"), method="sendChatMessage(Ljava/lang/String;)V", cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        // Handle commands etc.
        CheatClient.processChatPost(message, ci);

        CheatClient.emitter.triggerEvent(new PlayerChatEvent(message, ci));
    }

    @Inject(at = @At("HEAD"), method="sendMovementPackets()V", cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new MovementPacketEvent(ci));
    }

    @Inject(at = @At("RETURN"), method="tick()V", cancellable = false)
    private void onTick(CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new ClientTickEvent(ci));
    }
}