package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.util.math.MatrixStack;

public class BobViewWhenHurtEvent extends Event {
    public MatrixStack mStack;
    public float f;
    public CallbackInfo ci;

    public BobViewWhenHurtEvent(MatrixStack mStack, float f, CallbackInfo ci) {
        this.mStack = mStack;
        this.f = f;
        this.ci = ci;
    }
}
