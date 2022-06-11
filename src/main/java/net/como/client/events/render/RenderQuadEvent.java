package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class RenderQuadEvent extends Event {
    // BlockRenderView world, BlockState state, BlockPos pos, VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int light0, int light1, int light2, int light3, int overlay, CallbackInfo ci
    public BlockRenderView world;
    public BlockState state;
    public BlockPos pos;
    public VertexConsumer vertexConsumer;
    public MatrixStack.Entry matrixEntry;
    public BakedQuad quad;
    public float brightness0;
    public float brightness1;
    public float brightness2;
    public float brightness3;
    public int light0;
    public int light1;
    public int light2;
    public int light3;
    public int overlay;
    public CallbackInfo ci;

    public RenderQuadEvent(
        BlockRenderView world, BlockState state, BlockPos pos, VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int light0, int light1, int light2, int light3, int overlay, CallbackInfo ci
    ) {
        this.world = world;
        this.state = state;
        this.pos = pos;
        this.vertexConsumer = vertexConsumer;
        this.matrixEntry = matrixEntry;
        this.quad = quad;
        this.brightness0 = brightness0;
        this.brightness1 = brightness1;
        this.brightness2 = brightness2;
        this.brightness3 = brightness3;
        this.light0 = light0;
        this.light1 = light1;
        this.light2 = light2;
        this.light3 = light3;
        this.overlay = overlay;
        this.ci = ci;
    }
}
