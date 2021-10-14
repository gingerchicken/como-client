package net.como.client.utils;

import net.como.client.CheatClient;
import net.como.client.interfaces.mixin.IEntity;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
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
        CheatClient.getClient().interactionManager.attackEntity(CheatClient.me(), target);
        CheatClient.me().swingHand(Hand.MAIN_HAND);
    }

    public static Rotation getRotation() {
        return new Rotation(
            (double)CheatClient.me().getYaw(),
            (double)CheatClient.me().getPitch()
        );
    }

    public static void sendPos(double x, double y, double z, boolean onGround) {
        CheatClient.me().networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround)
        );
    }

    public static void sendPos(Vec3d pos, boolean onGround) {
        sendPos(pos.x, pos.y, pos.z, onGround);
    }

    public static Boolean inGame() {
        return CheatClient.me() != null;
    }

    public static Boolean isInNetherPortal() {
        IEntity me = (IEntity)CheatClient.me();
        
        return me.getInNetherPortal();
    }

    public static Boolean isThirdperson() {
        return CheatClient.getClient().gameRenderer.getCamera().isThirdPerson();
    }
}
