package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.ComoClient;
import net.como.client.events.client.OnAttackEntityEvent;
import net.como.client.events.client.StopUsingItemEvent;
import net.como.client.events.client.UpdateBlockBreakingProgressEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

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

    @Inject(at = @At("HEAD"), method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", cancellable = true)
    private void onUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        ComoClient.emitter.triggerEvent(new UpdateBlockBreakingProgressEvent(pos, direction, cir));
    }
}
