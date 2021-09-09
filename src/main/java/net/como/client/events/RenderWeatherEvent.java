package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.render.LightmapTextureManager;

public class RenderWeatherEvent extends Event {
    public LightmapTextureManager manager;
    public float f;
    public double d;
    public double e;
    public double g;
    public CallbackInfo ci;

    public RenderWeatherEvent(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        this.manager = manager;
        this.f = f;
        this.d = d;
        this.e = e;
        this.g = g;
        this.ci = ci;
    }
}
