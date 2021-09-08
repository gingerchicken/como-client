package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.render.Camera;

public class TickRainSplashingEvent extends Event {
    public Camera camera;
    public CallbackInfo ci;

    public TickRainSplashingEvent(Camera camera, CallbackInfo ci) {
        this.camera = camera;
        this.ci = ci;
    }
}
