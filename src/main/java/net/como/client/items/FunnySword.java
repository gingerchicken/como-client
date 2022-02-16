package net.como.client.items;

import net.como.client.structures.CreativeItem;
import net.como.client.utils.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class FunnySword implements CreativeItem {

    @Override
    public ItemStack getStack() {
        ItemStack item = new ItemStack(Items.DIAMOND_SWORD);
        
        NbtCompound nbt = new NbtCompound();
        NbtList enchants = new NbtList();
        
        // Add enchantments
        for (String e : NbtUtils.ENCHANTMENTS) {
            enchants.add(NbtUtils.enchantment(e, Short.MAX_VALUE));
        }

        nbt.put("Enchantments", enchants);
        item.setNbt(nbt);

        return item;
    }

    @Override
    public String getName() {
        return "B-Tech 32k Sword";
    }
    
}
