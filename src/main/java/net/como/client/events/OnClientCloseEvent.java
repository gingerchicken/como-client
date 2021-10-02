package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class OnClientCloseEvent extends Event {
    CallbackInfo ci;
    
    public OnClientCloseEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
