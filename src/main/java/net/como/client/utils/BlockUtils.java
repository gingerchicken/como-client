// Thanks to the devs at Wurst for this file - it saved me a bit of time :D

package net.como.client.utils;

import java.util.ArrayList;

import net.como.client.ComoClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class BlockUtils {
	private static final MinecraftClient client = ComoClient.getClient();
	
	public static BlockState getState(BlockPos pos) {
		return client.world.getBlockState(pos);
	}
	
	public static Block getBlock(BlockPos pos) {
		return getState(pos).getBlock();
	}
	
	// Get the minecraft friendly id.
	public static int getId(BlockPos pos) {
		return Block.getRawIdFromState(getState(pos));
	}
	
	public static String getName(BlockPos pos) {
		return getName(getBlock(pos));
	}
	
	public static String getName(Block block) {
		return Registry.BLOCK.getId(block).toString();
	}
	
	public static Block getBlockFromName(String name) {
		try {
			return Registry.BLOCK.get(new Identifier(name));
			
		} catch(InvalidIdentifierException e) {
			return Blocks.AIR;
		}
	}
	
	public static float getHardness(BlockPos pos) {
		return getState(pos).calcBlockBreakingDelta(client.player, client.world, pos);
	}
	
	private static VoxelShape getOutlineShape(BlockPos pos) {
		return getState(pos).getOutlineShape(client.world, pos);
	}
	
	public static Box getBoundingBox(BlockPos pos) {
		return getOutlineShape(pos).getBoundingBox().offset(pos);
	}
	
	public static boolean canBeClicked(BlockPos pos) {
		return getOutlineShape(pos) != VoxelShapes.empty();
	}
	
	public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
		ArrayList<BlockPos> blocks = new ArrayList<>();
		
		BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
		BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
		
		// Oh boy, O(n^3) :D
		for(int x = min.getX(); x <= max.getX(); x++)
			for(int y = min.getY(); y <= max.getY(); y++)
				for(int z = min.getZ(); z <= max.getZ(); z++)
					blocks.add(new BlockPos(x, y, z));
				
		return blocks;
	}

	public static Vec3d blockPos(BlockPos pos) {
		return new Vec3d(
			pos.getX(),
			pos.getY(),
			pos.getZ()
		);
	}
}