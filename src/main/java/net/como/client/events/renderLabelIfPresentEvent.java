package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class renderLabelIfPresentEvent<T extends Entity> extends Event {
    public T entity;
    public Text text;
	public MatrixStack mStack;
    public VertexConsumerProvider vertexConsumerProvider;
	public int i;
    public CallbackInfo ci;
    public EntityRenderDispatcher dispatcher;

    public renderLabelIfPresentEvent(T entity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, EntityRenderDispatcher dispatcher, CallbackInfo ci) {
        this.entity = entity;
        this.text = text;
        this.mStack = matrixStack;
        this.vertexConsumerProvider = vertexConsumerProvider;
        this.i = i;
        this.dispatcher = dispatcher;
        this.ci = ci;
    }
}
