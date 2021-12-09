package net.como.client.utils;

import net.como.client.ComoClient;
import net.como.client.interfaces.mixin.IEntity;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class ClientUtils {
    public static boolean isInStorage() {
        Screen screen = ComoClient.getClient().currentScreen;
        if (screen == null) return false;

        switch (screen.getClass().getSimpleName()) {
            case "ShulkerBoxScreen":
            case "GenericContainerScreen": return true;
            default: return false;
        }
    
    }

    public static boolean hasElytraEquipt() {
        ItemStack chestSlot = ComoClient.me().getEquippedStack(EquipmentSlot.CHEST);
		return (chestSlot.getItem() == Items.ELYTRA);
    }

    public static void applyRotation(Rotation rot) {
        ComoClient.me().setYaw((float)rot.yaw);
        ComoClient.me().setPitch((float)rot.pitch);
    }

    public static void lookAtPos(Vec3d pos) {
        applyRotation(
            RotationUtils.getRequiredRotation(pos)
        );
    }

    public static void hitEntity(Entity target) {
        ComoClient.getClient().interactionManager.attackEntity(ComoClient.me(), target);
        ComoClient.me().swingHand(Hand.MAIN_HAND);
    }

    public static Rotation getRotation() {
        return new Rotation(
            (double)ComoClient.me().getYaw(),
            (double)ComoClient.me().getPitch()
        );
    }

    public static void sendPos(double x, double y, double z, boolean onGround) {
        ComoClient.me().networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround)
        );
    }

    public static void sendPos(Vec3d pos, boolean onGround) {
        sendPos(pos.x, pos.y, pos.z, onGround);
    }

    public static Boolean inGame() {
        return ComoClient.me() != null;
    }

    public static Boolean isInNetherPortal() {
        IEntity me = (IEntity)ComoClient.me();
        
        return me.getInNetherPortal();
    }

    public static Boolean isThirdperson() {
        return ComoClient.getClient().gameRenderer.getCamera().isThirdPerson();
    }

    public static void openChatScreen(String text) {
        if (!inGame()) return;

        ComoClient.getClient().setScreen(new ChatScreen(text));
    }

    public static void openChatScreen() {
        openChatScreen("");
    }

    public static ItemStack getHandlerSlot(int i) {
        return ComoClient.me().currentScreenHandler.getSlot(i).getStack();
    }
}
