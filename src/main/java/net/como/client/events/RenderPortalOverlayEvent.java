package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class RenderPortalOverlayEvent extends Event {
    public float nauseaStrength;
    public CallbackInfo ci;

    public RenderPortalOverlayEvent(float nauseaStrength, CallbackInfo ci) {
        this.nauseaStrength = nauseaStrength;
        this.ci = ci;
    }
}
