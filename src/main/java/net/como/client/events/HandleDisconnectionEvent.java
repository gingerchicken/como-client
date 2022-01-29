package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class HandleDisconnectionEvent extends Event {
    public CallbackInfo ci;
    
    public HandleDisconnectionEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
