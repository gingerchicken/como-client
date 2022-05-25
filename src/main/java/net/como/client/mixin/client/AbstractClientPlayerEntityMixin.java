package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.ComoClient;
import net.como.client.events.render.GetModelEvent;
import net.como.client.events.render.GetSkinTextureEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "getSkinTexture()Lnet/minecraft/util/Identifier;", cancellable = true)
    private void onGetSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        ComoClient.emitter.triggerEvent(new GetSkinTextureEvent((AbstractClientPlayerEntity)(Object)(this), cir));
    }

    @Inject(at = @At("RETURN"), method = "getModel()Ljava/lang/String;", cancellable = true)
    private void onGetModel(CallbackInfoReturnable<String> cir) {
        ComoClient.emitter.triggerEvent(new GetModelEvent((AbstractClientPlayerEntity)(Object)(this), cir));
    }
}
