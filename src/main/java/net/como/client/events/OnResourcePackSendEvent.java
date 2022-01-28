package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;

public class OnResourcePackSendEvent extends Event {
    public ResourcePackSendS2CPacket packet;
    public CallbackInfo ci;
    
    public OnResourcePackSendEvent(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        this.packet = packet;
        this.ci = ci;
    }
}
