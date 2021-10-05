package net.como.client.interfaces.mixin;

import java.util.List;

import net.minecraft.world.chunk.BlockEntityTickInvoker;

public interface IWorld {
    List<BlockEntityTickInvoker> getBlockEntityTickers();
}
