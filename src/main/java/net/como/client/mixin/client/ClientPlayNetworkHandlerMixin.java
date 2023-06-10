package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.client.OnDisconnectedEvent;
import net.como.client.events.packet.OnEntityStatusEvent;
import net.como.client.events.packet.OnGameStateChangeEvent;
import net.como.client.events.packet.OnResourcePackSendEvent;
import net.como.client.events.packet.OnWorldTimeUpdateEvent;
import net.como.client.events.packet.SendPacketEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.text.Text;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method="sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new SendPacketEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onWorldTimeUpdate(Lnet/minecraft/network/packet/s2c/play/WorldTimeUpdateS2CPacket;)V", cancellable=true)
    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new OnWorldTimeUpdateEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onEntityStatus(Lnet/minecraft/network/packet/s2c/play/EntityStatusS2CPacket;)V", cancellable = true)
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new OnEntityStatusEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onGameStateChange(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket;)V", cancellable = true)
    public void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new OnGameStateChangeEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onResourcePackSend(Lnet/minecraft/network/packet/s2c/play/ResourcePackSendS2CPacket;)V", cancellable = true)
    public void onResourcePackSend(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new OnResourcePackSendEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onDisconnected(Lnet/minecraft/text/Text;)V", cancellable = true)
    public void onDisconnected(Text reason, CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new OnDisconnectedEvent(reason, ci));
    }
}
