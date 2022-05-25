package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.misc.events.Event;

public class HandleDisconnectionEvent extends Event {
    public CallbackInfo ci;
    
    public HandleDisconnectionEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
