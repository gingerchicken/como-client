package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@Mixin(Block.class)
public abstract class BlockMixin implements ItemConvertible {
	@Inject(at = {@At("RETURN")}, 
        method = {
            "shouldDrawSide(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"
        },
		cancellable = true
    )
	private static void onShouldDrawSide(BlockState state, BlockView blockView, BlockPos blockPos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        CheatClient.triggerAllEvent("onShouldDrawBlockSide", new Object[] {
            state, blockView, blockPos, side, cir
        });
	}
	
	@Inject(at = {@At("HEAD")}, method = {"getVelocityMultiplier()F"}, cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        CheatClient.triggerAllEvent("onGetVelocityMultiplier", new Object[] {
            cir
        });
	}
}