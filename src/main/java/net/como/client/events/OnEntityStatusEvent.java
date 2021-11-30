package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class OnEntityStatusEvent extends Event {
    public CallbackInfo ci;
    public EntityStatusS2CPacket packet;

    public OnEntityStatusEvent(EntityStatusS2CPacket packet, CallbackInfo ci) {
        this.packet = packet;
        this.ci = ci;
    }
}
