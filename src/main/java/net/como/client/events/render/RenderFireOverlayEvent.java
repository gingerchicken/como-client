package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class RenderFireOverlayEvent extends Event {
    public MinecraftClient client;
    public MatrixStack mStack;
    public CallbackInfo ci;

    public RenderFireOverlayEvent(MinecraftClient client, MatrixStack mStack, CallbackInfo ci) {
        this.client = client;
        this.mStack = mStack;
        this.ci = ci;
    }
}
