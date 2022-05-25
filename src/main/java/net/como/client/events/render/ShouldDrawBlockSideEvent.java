package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class ShouldDrawBlockSideEvent extends Event {
    public BlockState state;
    public BlockView blockView;
    public BlockPos pos;
    public Direction side;
    public BlockPos blockPos;
    public CallbackInfoReturnable<Boolean> cir;

    public ShouldDrawBlockSideEvent(BlockState state, BlockView blockView, BlockPos pos, Direction side, BlockPos blockPos,  CallbackInfoReturnable<Boolean> cir) {
        this.state = state;
        this.blockView = blockView;
        this.pos = pos;
        this.side = side;
        this.blockPos = blockPos;
        this.cir = cir;
    }
}
