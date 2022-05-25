package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.misc.events.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class GetAmbientOcclusionLightLevelEvent extends Event {
    public BlockView blockView;
    public BlockPos blockPos;
    public CallbackInfoReturnable<Float> cir;

    public GetAmbientOcclusionLightLevelEvent(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        this.cir = cir;
        this.blockView = blockView;
        this.blockPos = blockPos;
    }
}
