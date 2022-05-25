package net.como.client.events.packet;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.misc.events.Event;

public class PostMovementPacketEvent extends Event {
    public CallbackInfo ci;

    public PostMovementPacketEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
