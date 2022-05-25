package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;

public class AddParticleEmitterEvent extends Event {
    public CallbackInfo ci;

    public AddParticleEmitterEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
