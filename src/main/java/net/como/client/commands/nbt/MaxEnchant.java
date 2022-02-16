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

        for (String id : NbtUtils.ENCHANTMENTS) {
            enchants.add(NbtUtils.enchantment(id, Short.MAX_VALUE));
        }

        nbt.put("Enchantments", enchants);

        itemStack.setNbt(nbt);

        return true;
    }
}
