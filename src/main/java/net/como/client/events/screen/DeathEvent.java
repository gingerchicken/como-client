package net.como.client.events.screen;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.misc.events.Event;

public class DeathEvent extends Event {
    public CallbackInfo ci;

    public DeathEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
