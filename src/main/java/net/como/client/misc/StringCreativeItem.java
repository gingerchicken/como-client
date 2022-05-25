package net.como.client.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

public interface StringCreativeItem extends CreativeItem {
    public String getNbtString();
    public Item getItem();

    @Override
    default ItemStack getStack() {
        ItemStack stack = new ItemStack(this.getItem());

        NbtCompound nbt = new NbtCompound();
        try {
            nbt = StringNbtReader.parse(this.getNbtString());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        stack.setNbt(nbt);

        return stack;
    }

    @Override
    default boolean useName() {
        return false;
    }
}