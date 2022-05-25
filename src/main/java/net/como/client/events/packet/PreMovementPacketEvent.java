package net.como.client.events.packet;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.misc.events.Event;

public class PreMovementPacketEvent extends Event {
    public CallbackInfo ci;

    public PreMovementPacketEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
