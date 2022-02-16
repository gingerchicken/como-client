package net.como.client.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

public class NbtUtils {
    public static NbtCompound enchantment(String enchantmentName, Short level) {
        NbtCompound enchant = new NbtCompound();

        enchant.put("id", NbtString.of(enchantmentName));
        enchant.put("lvl", NbtShort.of(level));

        return enchant;
    }

    public static final String[] ENCHANTMENTS = {"minecraft:protection","minecraft:fire_protection","minecraft:feather_falling","minecraft:blast_protection","minecraft:projectile_protection","minecraft:respiration","minecraft:aqua_affinity","minecraft:thorns","minecraft:depth_strider","minecraft:sharpness","minecraft:smite","minecraft:bane_of_arthropods","minecraft:knockback","minecraft:fire_aspect","minecraft:looting","minecraft:efficiency","minecraft:silk_touch","minecraft:unbreaking","minecraft:fortune","minecraft:power","minecraft:punch","minecraft:flame","minecraft:infinity","minecraft:luck_of_the_sea","minecraft:lure"};
}
