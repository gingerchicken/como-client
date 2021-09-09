package net.como.client.utils;

import net.como.client.CheatClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ClientUtils {
    public static boolean hasElytraEquipt() {
        ItemStack chestSlot = CheatClient.me().getEquippedStack(EquipmentSlot.CHEST);
		return (chestSlot.getItem() == Items.ELYTRA);
    }
}
