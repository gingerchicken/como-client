package net.como.client.structures;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface CreativeItem {
    public ItemStack getStack();
    public String getName();
    default public boolean useName() {
        return true;
    }
    default public ItemStack readyStack() {
        ItemStack stack = this.getStack();
        stack.setCustomName(Text.of(this.getName()));

        return stack;
    }
}
