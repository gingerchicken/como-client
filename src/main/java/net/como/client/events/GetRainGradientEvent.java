package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;

public class GetRainGradientEvent extends Event {
    public CallbackInfoReturnable<Float> cir;
    public float delta;

    public GetRainGradientEvent(float delta, CallbackInfoReturnable<Float> cir) {
        this.delta = delta;
        this.cir = cir;
    }
}
