package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class OnAttackEntityEvent extends Event {
    public PlayerEntity player;
    public Entity target;
    public CallbackInfo ci;

    public OnAttackEntityEvent(PlayerEntity player, Entity target, CallbackInfo ci) {
        this.player = player;
        this.target = target;
        this.ci = ci;
    }
}
