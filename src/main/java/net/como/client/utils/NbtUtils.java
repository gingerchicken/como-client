package net.como.client.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

public class NbtUtils {
    public static NbtCompound enchantment(String enchantmentName, Short level) {
        NbtCompound enchant = new NbtCompound();

        enchant.put("id", NbtString.of(enchantmentName));
        enchant.put("lvl", NbtShort.of(level));

        return enchant;
    }
}
