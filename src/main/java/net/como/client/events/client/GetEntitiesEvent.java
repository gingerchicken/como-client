package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.events.Event;
import net.minecraft.entity.Entity;

public class GetEntitiesEvent extends Event {
    public CallbackInfoReturnable<Iterable<Entity>> cir;
    
    public GetEntitiesEvent(CallbackInfoReturnable<Iterable<Entity>> cir) {
        this.cir = cir;
    }
}
