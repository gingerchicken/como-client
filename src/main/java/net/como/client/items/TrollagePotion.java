package net.como.client.items;

import net.como.client.structures.CreativeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class TrollagePotion implements CreativeItem {
    public Item itemType = Items.SPLASH_POTION;

    @Override
    public ItemStack getStack() {
        ItemStack stack = new ItemStack(itemType);
			
        NbtCompound effect = new NbtCompound();
        effect.putInt("Amplifier", 125);
        effect.putInt("Id", 6);
        effect.putInt("Duration", 2000);
        
        NbtList effects = new NbtList();
        effects.add(effect);
        
        NbtCompound nbt = new NbtCompound();
        nbt.put("CustomPotionEffects", effects);
        stack.setNbt(nbt);
        
        return stack;
    }

    @Override
    public String getName() {
        return "Splash Trollage Potion";
    }
    
}
