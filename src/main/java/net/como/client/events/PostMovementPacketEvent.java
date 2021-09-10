package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class PostMovementPacketEvent extends Event {
    public CallbackInfo ci;

    public PostMovementPacketEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}