package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.misc.events.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEntityEvent extends Event {
    public net.minecraft.entity.Entity entity;
    public double cameraX;
    public double cameraY;
    public double cameraZ;
    public float tickDelta;
    public MatrixStack mStack;
    public VertexConsumerProvider vertexConsumers;
    public CallbackInfo ci;

    public RenderEntityEvent(net.minecraft.entity.Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack mStack, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        this.entity = entity;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraZ = cameraZ;
        this.tickDelta = tickDelta;
        this.mStack = mStack;
        this.vertexConsumers = vertexConsumers;
        this.ci = ci;
    }
}
