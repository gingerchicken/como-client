package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;

public class OnClientCloseEvent extends Event {
    CallbackInfo ci;
    
    public OnClientCloseEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
