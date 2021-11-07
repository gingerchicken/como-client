package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class OnWorldTimeUpdateEvent extends Event {
    public CallbackInfo ci;
    public WorldTimeUpdateS2CPacket packet;

    public OnWorldTimeUpdateEvent(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        this.ci = ci;
        this.packet = packet;
    }
}
