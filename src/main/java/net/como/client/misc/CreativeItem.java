package net.como.client.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface CreativeItem {
    public ItemStack getStack();
    default public String getName() {
        return this.getClass().getName();
    }
    default public boolean useName() {
        return true;
    }
    default public ItemStack readyStack() {
        ItemStack stack = this.getStack();
        if (this.useName()) stack.setCustomName(Text.of(this.getName()));

        return stack;
    }
    default public Boolean isLaggy() {
        return false;
    }
}
