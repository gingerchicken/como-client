package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.misc.events.Event;

public class BossBarHudSkyEffectsEvent extends Event {
    public CallbackInfoReturnable<Boolean> cir;

    public BossBarHudSkyEffectsEvent(CallbackInfoReturnable<Boolean> cir) {
        this.cir = cir;
    }
}
