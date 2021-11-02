package net.como.client.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.CheatClient;
import net.como.client.events.IsEntityInvisibleEvent;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("RETURN"), method="isInvisible()Z", cancellable = true)
    public void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        CheatClient.emitter.triggerEvent(new IsEntityInvisibleEvent(cir));
    }
}
