package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.misc.events.Event;

public class RenderPortalOverlayEvent extends Event {
    public float nauseaStrength;
    public CallbackInfo ci;

    public RenderPortalOverlayEvent(float nauseaStrength, CallbackInfo ci) {
        this.nauseaStrength = nauseaStrength;
        this.ci = ci;
    }
}
