package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class JumpEvent extends Event {
    public CallbackInfo ci;

    public JumpEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
