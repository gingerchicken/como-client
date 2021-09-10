package net.como.client.utils;

import net.como.client.CheatClient;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class ClientUtils {
    public static boolean hasElytraEquipt() {
        ItemStack chestSlot = CheatClient.me().getEquippedStack(EquipmentSlot.CHEST);
		return (chestSlot.getItem() == Items.ELYTRA);
    }

    public static void applyRotation(Rotation rot) {
        CheatClient.me().setYaw((float)rot.yaw);
        CheatClient.me().setPitch((float)rot.pitch);
    }

    public static void lookAtPos(Vec3d pos) {
        applyRotation(
            RotationUtils.getRequiredRotation(pos)
        );
    }

    public static void hitEntity(Entity target) {
        CheatClient.me().swingHand(Hand.MAIN_HAND);
        CheatClient.getClient().interactionManager.attackEntity(CheatClient.me(), target);
    }

    public static Rotation getRotation() {
        return new Rotation(
            (double)CheatClient.me().getYaw(),
            (double)CheatClient.me().getPitch()
        );
    }
}
