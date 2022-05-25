package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;
import net.minecraft.entity.Entity;

public class IsEntityGlowingEvent extends Event {
    public CallbackInfoReturnable<Boolean> cir;
    public Entity entity;

    public IsEntityGlowingEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        this.entity = entity;
        this.cir = cir;
    }
}
