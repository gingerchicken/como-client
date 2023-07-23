package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class InGameHudRenderEvent extends Event {
    public DrawContext context;
    public float tickDelta;
    public CallbackInfo ci;

    public InGameHudRenderEvent(DrawContext context, float tickDelta, CallbackInfo ci) {
        this.context = context;
        this.tickDelta = tickDelta;
        this.ci = ci;
    }
}
