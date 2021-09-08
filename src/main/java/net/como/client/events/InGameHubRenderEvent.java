package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.util.math.MatrixStack;

public class InGameHubRenderEvent extends Event {
    public MatrixStack mStack;
    public float tickDelta;
    public CallbackInfo ci;

    public InGameHubRenderEvent(MatrixStack mStack, float tickDelta, CallbackInfo ci) {
        this.mStack = mStack;
        this.tickDelta = tickDelta;
        this.ci = ci;
    }
}
