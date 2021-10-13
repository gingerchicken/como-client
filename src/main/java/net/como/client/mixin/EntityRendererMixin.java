package net.como.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.events.renderLabelIfPresentEvent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity>
{
    @Shadow
    @Final
    EntityRenderDispatcher dispatcher;

	@Inject(at = {@At("HEAD")},
		method = {
			"renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
        },
        cancellable = true
    )
	private void onRenderLabelIfPresent(
        T entity,
        Text text,
		MatrixStack matrixStack,
        VertexConsumerProvider vertexConsumerProvider,
		int i, CallbackInfo ci
    ) {
		CheatClient.emitter.triggerEvent(
            new renderLabelIfPresentEvent<T>(
                entity, text, matrixStack, vertexConsumerProvider, i, this.dispatcher, ci
            )
        );
	}

}