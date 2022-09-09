package net.como.client.mixin.client;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.client.ClientTickMovementEvent;
import net.como.client.events.client.PlayerChatEvent;
import net.como.client.events.client.PlayerMoveEvent;
import net.como.client.events.packet.PostMovementPacketEvent;
import net.como.client.events.packet.PreMovementPacketEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(at = @At("HEAD"), method="move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
    private void onMove(MovementType type, Vec3d offset, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new PlayerMoveEvent(type, offset, ci));
    }

    @Inject(at = @At("HEAD"), method="sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V", cancellable = true)
    private void onSendChatMessage(String message, Text preview, CallbackInfo ci) {
        // Handle commands etc.
        ComoClient.getInstance().processChatPost(message, ci);

        ComoClient.getInstance().emitter.triggerEvent(new PlayerChatEvent(message, ci));
    }

    @Inject(at = @At("HEAD"), method="sendMovementPackets()V", cancellable = true)
    private void beforeSendMovementPackets(CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new PreMovementPacketEvent(ci));
    }

    @Inject(at = @At("TAIL"), method="sendMovementPackets()V", cancellable = false)
    private void afterSendMovementPackets(CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new PostMovementPacketEvent(ci));
    }

    @Inject(at = @At("RETURN"), method="tick()V", cancellable = false)
    private void onTick(CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new ClientTickEvent(ci));
    }

    @Inject(at = @At("HEAD"), method="tickMovement()V", cancellable = false)
    private void onTickMovement(CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new ClientTickMovementEvent(ci));
    }
}