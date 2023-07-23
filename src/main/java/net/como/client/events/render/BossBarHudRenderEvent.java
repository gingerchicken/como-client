package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.client.gui.DrawContext;

public class BossBarHudRenderEvent extends Event {
    public CallbackInfo ci;
    public DrawContext context;

    public BossBarHudRenderEvent(DrawContext context, CallbackInfo ci) {
        this.ci = ci;
        this.context = context;
    }
}
