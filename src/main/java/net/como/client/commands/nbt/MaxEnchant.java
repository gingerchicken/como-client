package net.como.client.commands.nbt;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.utils.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class MaxEnchant extends Command {

    public MaxEnchant() {
        super("maxenchant", "", "Gives the current item every enchantment");
    }

    @Override
    public Boolean trigger(String[] args) {
        ItemStack itemStack = ComoClient.me().getMainHandStack();

        NbtCompound nbt = new NbtCompound();
        NbtList enchants = new NbtList();

        String[] enchantments = {"minecraft:protection","minecraft:fire_protection","minecraft:feather_falling","minecraft:blast_protection","minecraft:projectile_protection","minecraft:respiration","minecraft:aqua_affinity","minecraft:thorns","minecraft:depth_strider","minecraft:sharpness","minecraft:smite","minecraft:bane_of_arthropods","minecraft:knockback","minecraft:fire_aspect","minecraft:looting","minecraft:efficiency","minecraft:silk_touch","minecraft:unbreaking","minecraft:fortune","minecraft:power","minecraft:punch","minecraft:flame","minecraft:infinity","minecraft:luck_of_the_sea","minecraft:lure"};

        for (String id : enchantments) {
            enchants.add(NbtUtils.enchantment(id, Short.MAX_VALUE));
        }

        nbt.put("Enchantments", enchants);

        itemStack.setNbt(nbt);

        return true;
    }
}
