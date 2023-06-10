package net.como.client.utils;

import net.como.client.ComoClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class InteractionUtils {
    public static ClientPlayerInteractionManager getManager() {        
        return ComoClient.getClient().interactionManager;
    }

    public static int getSyncId() {
        return ComoClient.me().currentScreenHandler.syncId;
    }

    public static void pickupItem(int slot) {
        getManager().clickSlot(getSyncId(), slot, 0, SlotActionType.PICKUP, ComoClient.me());
    }

    public static void fastPickupItem(int slot) {
        getManager().clickSlot(getSyncId(), slot, 0, SlotActionType.QUICK_MOVE, ComoClient.me());
    }

    public static void rightClickBlock(BlockPos pos, Direction side, Vec3d hitVec) {
        ClientPlayerInteractionManager im = getManager();
        MinecraftClient client = ComoClient.getClient();
        ClientPlayerEntity me = ComoClient.me();

        // Send block packet
        im.interactBlock(me, Hand.MAIN_HAND, new BlockHitResult(hitVec, side, pos, false));

        // Send item packet
		im.interactItem(client.player, Hand.MAIN_HAND);
    }

    public static void fireActive(Hand hand) {
        ClientWorld world = ComoClient.me().clientWorld;

        BlockHitResult result = new BlockHitResult(
            ComoClient.me().getPos(),
            Direction.DOWN,
            ComoClient.me().getBlockPos(),
            false
        );

        // Create the interact packet
        PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket(
            hand,
            result,
            ClientUtils.incrementPendingUpdateManager(world)
        );

        // Send the packet
        ComoClient.getClient().getNetworkHandler().sendPacket(packet);
    }
}
