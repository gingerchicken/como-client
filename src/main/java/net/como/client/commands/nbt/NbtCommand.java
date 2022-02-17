package net.como.client.commands.nbt;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.InventoryUtils;
import net.como.client.utils.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

public class NbtCommand extends CommandNode {

    public NbtCommand() {
        super("nbt", "Manipulate items' NBT data");

        this.addSubCommand(new MaxEnchant());
    }

    public static abstract class NbtModCommand extends Command {

        public NbtModCommand(String command, String helpText, String description) {
            super(command, helpText, description);
        }

        protected ItemStack heldItem() {
            return ComoClient.me().getMainHandStack();
        }

        public void clickHand(ItemStack stack) {
            ComoClient.me().networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(InventoryUtils.getMainHandSlot(), stack));
        }

        public void clickHand() {
            this.clickHand(this.heldItem());
        }
    }

    public static class MaxEnchant extends NbtModCommand {
        public MaxEnchant() {
            super("maxenchant", "", "Gives the current item every enchantment");
        }
    
        @Override
        public Boolean trigger(String[] args) {
            ItemStack itemStack = this.heldItem();
    
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
