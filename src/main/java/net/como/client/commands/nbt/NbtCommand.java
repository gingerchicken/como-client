package net.como.client.commands.nbt;

import net.como.client.commands.structures.CommandNode;

public class NbtCommand extends CommandNode {

    public NbtCommand() {
        super("nbt", "Manipulate items' NBT data");

        this.addSubCommand(new MaxEnchant());
    }

    public static class MaxEnchant extends Command {

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
}
