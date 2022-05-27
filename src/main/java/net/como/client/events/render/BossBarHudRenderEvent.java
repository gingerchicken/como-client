package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;

public class BossBarHudRenderEvent extends Event {
    public CallbackInfo ci;

    public BossBarHudRenderEvent(CallbackInfo ci) {
        this.ci = ci;
    }
}
