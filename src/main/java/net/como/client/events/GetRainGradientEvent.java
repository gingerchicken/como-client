package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;

public class GetRainGradientEvent extends Event {
    public CallbackInfoReturnable<Float> cir;

    public GetRainGradientEvent(CallbackInfoReturnable<Float> cir) {
        this.cir = cir;
    }
}
