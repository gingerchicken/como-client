package net.como.client.utils;

import net.como.client.CheatClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class InteractionUtils {
    public static ClientPlayerInteractionManager getManager() {        
        return CheatClient.getClient().interactionManager;
    }

    public static void pickupItem(int slot) {
        getManager().clickSlot(0, slot, 0, SlotActionType.PICKUP, CheatClient.me());
    }

    public static void rightClickBlock(BlockPos pos, Direction side, Vec3d hitVec) {
        ClientPlayerInteractionManager im = getManager();
        MinecraftClient client = CheatClient.getClient();
        ClientPlayerEntity me = CheatClient.me();

        // Send block packet
        im.interactBlock(me, client.world, Hand.MAIN_HAND, new BlockHitResult(hitVec, side, pos, false));

        // Send item packet
		im.interactItem(client.player, client.world, Hand.MAIN_HAND);
    }
}
