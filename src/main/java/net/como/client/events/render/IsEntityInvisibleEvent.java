package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.events.Event;

public class IsEntityInvisibleEvent extends Event {
    public CallbackInfoReturnable<Boolean> cir;

    public IsEntityInvisibleEvent(CallbackInfoReturnable<Boolean> cir) {
        this.cir = cir;
    }
}
