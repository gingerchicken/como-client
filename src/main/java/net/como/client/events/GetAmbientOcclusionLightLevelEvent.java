package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import net.como.client.structures.events.Event;

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
