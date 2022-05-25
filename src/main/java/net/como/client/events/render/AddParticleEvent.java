package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.events.Event;
import net.minecraft.client.particle.Particle;

public class AddParticleEvent extends Event {
    public CallbackInfoReturnable<Particle> ci;

    public AddParticleEvent(CallbackInfoReturnable<Particle> ci) {
        this.ci = ci;
    }
}
