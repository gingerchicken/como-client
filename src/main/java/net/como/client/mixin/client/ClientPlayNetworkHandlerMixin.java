package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.como.client.events.OnEntityStatusEvent;
import net.como.client.events.OnWorldTimeUpdateEvent;
import net.como.client.events.SendPacketEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method="sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new SendPacketEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onWorldTimeUpdate(Lnet/minecraft/network/packet/s2c/play/WorldTimeUpdateS2CPacket;)V", cancellable=true)
    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new OnWorldTimeUpdateEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onEntityStatus(Lnet/minecraft/network/packet/s2c/play/EntityStatusS2CPacket;)V", cancellable = true)
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new OnEntityStatusEvent(packet, ci));
    }
}
