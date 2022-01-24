package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;

public class GetClientModNameEvent extends Event {
    public CallbackInfoReturnable<String> cir;

    public GetClientModNameEvent(CallbackInfoReturnable<String> cir) {
        this.cir = cir;
    }
}
