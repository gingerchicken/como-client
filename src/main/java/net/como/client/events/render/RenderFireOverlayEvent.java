package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.como.client.structures.events.Event;

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
