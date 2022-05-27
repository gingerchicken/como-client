package net.como.client.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.ComoClient;
import net.como.client.events.render.IsEntityGlowingEvent;
import net.como.client.events.render.IsEntityInvisibleEvent;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("RETURN"), method="isInvisible()Z", cancellable = true)
    public void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        ComoClient.getInstance().emitter.triggerEvent(new IsEntityInvisibleEvent(cir));
    }

    @Inject(at = @At("RETURN"), method="isGlowing()Z", cancellable = true)
    public void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity ent = (Entity)((Object)(this));

        ComoClient.getInstance().emitter.triggerEvent(new IsEntityGlowingEvent(ent, cir));
    }
}
