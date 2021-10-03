package net.como.client.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.como.client.events.BobViewWhenHurtEvent;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderWorldViewBobbingEvent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At("HEAD"), method="bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
    void onBobViewWhenHurt(MatrixStack mStack, float f, CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new BobViewWhenHurtEvent(mStack, f, ci));
    }

    @Inject(
		at = {@At(value = "FIELD",
			target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0)},
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"
        },
        cancellable = true
    )
    private void onRender(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new OnRenderEvent(
            tickDelta, limitTime, matrix, ci
        ));
    }

    @Redirect(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V",
		ordinal = 0),
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"})
	private void onRenderWorldViewBobbing(GameRenderer gameRenderer, MatrixStack matrixStack, float partalTicks) {
        RenderWorldViewBobbingEvent event = new RenderWorldViewBobbingEvent(gameRenderer, matrixStack, partalTicks);

        CheatClient.emitter.triggerEvent(event);

        if (event.cancel) {
            return;
        }

		bobView(matrixStack, partalTicks);
	}

    @Shadow
	private void bobView(MatrixStack matrixStack, float partalTicks) {
		
	}
}
