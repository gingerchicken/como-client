package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class ClientTickEvent extends Event {
    public CallbackInfo ci;

    public ClientTickEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
