package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

@Mixin(AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(at = @At("RETURN"),
        method = {
            "getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"
        },
        cancellable = true
    )
    private void onGetAmbientOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        CheatClient.triggerAllEvent("onGetAmbientOcclusionLightLevel", new Object[] {
            blockView, blockPos, cir
        });
    }

    @Inject(at = @At("HEAD"),
		method = {
			"getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"
        },
		cancellable = true
    )
	private void onGetOutlineShape(BlockView view, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        CheatClient.triggerAllEvent("onGetOutlineShape", new Object[] {
            view, pos, context, cir
        });
	}
}
