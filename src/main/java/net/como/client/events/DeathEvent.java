package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class DeathEvent extends Event {
    public CallbackInfo ci;

    public DeathEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
