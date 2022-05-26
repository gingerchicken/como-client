package net.como.client.events.io;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;

public class OnMouseButtonEvent extends Event {
    public long window;

    public int button;
    public int action;
    public int mods;

    public CallbackInfo ci;

    public OnMouseButtonEvent(long window, int button, int action, int mods, CallbackInfo ci) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods   = mods;
        this.ci     = ci;
    }
}
