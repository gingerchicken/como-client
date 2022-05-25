package net.como.client.items.impl;

import net.como.client.items.CreativeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

public class CrashSack implements CreativeItem {

    @Override
    public ItemStack getStack() {
        ItemStack stack = new ItemStack(Items.BUNDLE);

        NbtCompound nbt = new NbtCompound();
        NbtList items = new NbtList();

        NbtCompound item = new NbtCompound();
        item.put("Count", NbtShort.of((short)64));
        item.put("id", NbtString.of("minecraft:grass_block"));

        for (int i = 0; i < Short.MAX_VALUE / 4; i++) {
            items.add(item);
        }

        nbt.put("Items", items);

        stack.setNbt(nbt);

        return stack;
    }

    @Override
    public Boolean isLaggy() {
        return true;
    }
}
