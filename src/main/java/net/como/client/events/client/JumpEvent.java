package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;

public class JumpEvent extends Event {
    public CallbackInfo ci;

    public JumpEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
