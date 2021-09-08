package net.como.client.events;

import java.util.Random;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class BlockCracksRenderEvent extends Event {
    public BlockRenderView world;
    public BakedModel model;
    public BlockState state;
    public BlockPos pos;
    public MatrixStack mStack;
    public VertexConsumer vertexConsumer;
    public boolean cull;
    public Random random;
    public long seed;
    public int overlay;
    public CallbackInfoReturnable<Boolean> cir;

    public BlockCracksRenderEvent(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack mStack, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, CallbackInfoReturnable<Boolean> cir) {
        this.world = world;
        this.model = model;
        this.state = state;
        this.pos = pos;
        this.mStack = mStack;
        this.vertexConsumer = vertexConsumer;
        this.cull = cull;
        this.random = random;
        this.seed = seed;
        this.overlay = overlay;
        this.cir = cir;
    }
}
