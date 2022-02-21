package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;

public class GetVelocityMultiplierEvent extends Event {
    public CallbackInfoReturnable<Float> cir;
    
    public GetVelocityMultiplierEvent(CallbackInfoReturnable<Float> cir) {
        this.cir = cir;
    }
}
