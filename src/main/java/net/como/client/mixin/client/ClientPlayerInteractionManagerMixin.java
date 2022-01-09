package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.OnAttackEntityEvent;
import net.como.client.events.StopUsingItemEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(at = @At("HEAD"), method = "attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V", cancellable = true)
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new OnAttackEntityEvent(player, target, ci));
    }

    @Inject(at = @At("HEAD"), method = "stopUsingItem(Lnet/minecraft/entity/player/PlayerEntity;)V", cancellable = true)
    private void onStopUsingItem(PlayerEntity playerEntity, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new StopUsingItemEvent(playerEntity, ci));
    }
}
