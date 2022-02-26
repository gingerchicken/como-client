package net.como.client.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.ComoClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

public class NbtUtils {
    public static NbtCompound enchantment(String enchantmentName, Short level) {
        NbtCompound enchant = new NbtCompound();

        enchant.put("id", NbtString.of(enchantmentName));
        enchant.put("lvl", NbtShort.of(level));

        return enchant;
    }

    public static void giveItem(ItemStack item) {
        giveItem(item, true);
    }

    public static void giveItem(ItemStack item, Boolean refreshInv) {
        Integer slot = InventoryUtils.getMainHandSlot();

        // Send the packet
        CreativeInventoryActionC2SPacket packet = new CreativeInventoryActionC2SPacket(slot, item);
        ComoClient.getClient().getNetworkHandler().sendPacket(packet);

        if (refreshInv) ClientUtils.refreshInventory();
    }

    public static final String[] ENCHANTMENTS = {"minecraft:protection","minecraft:fire_protection","minecraft:feather_falling","minecraft:blast_protection","minecraft:projectile_protection","minecraft:respiration","minecraft:aqua_affinity","minecraft:thorns","minecraft:depth_strider","minecraft:sharpness","minecraft:smite","minecraft:bane_of_arthropods","minecraft:knockback","minecraft:fire_aspect","minecraft:looting","minecraft:efficiency","minecraft:silk_touch","minecraft:unbreaking","minecraft:fortune","minecraft:power","minecraft:punch","minecraft:flame","minecraft:infinity","minecraft:luck_of_the_sea","minecraft:lure"};
}
