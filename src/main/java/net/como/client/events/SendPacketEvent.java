package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.network.Packet;

public class SendPacketEvent extends Event {
    public CallbackInfo ci;
    public Packet<?> packet;

    public SendPacketEvent(Packet<?> packet, CallbackInfo ci) {
        this.ci = ci;
        this.packet = packet;
    }
}
