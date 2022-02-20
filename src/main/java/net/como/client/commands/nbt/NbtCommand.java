package net.como.client.commands.nbt;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.NbtUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class NbtCommand extends CommandNode {

    public NbtCommand() {
        super("nbt", "Manipulate items' NBT data");

        this.addSubCommand(new MaxEnchant());
        this.addSubCommand(new SetStack());
        this.addSubCommand(new Show());
        this.addSubCommand(new NbtCopy());
    }

    public static abstract class NbtModCommand extends Command {

        public NbtModCommand(String command, String helpText, String description) {
            super(command, helpText, description);
        }

        protected ItemStack heldItem() {
            return ComoClient.me().getMainHandStack();
        }

        public String getItemNbtAsString() {
            NbtCompound nbt = this.heldItem().getNbt();

            if (nbt == null) {
                return "{}";
            }

            return nbt.toString();
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
    public static class SetStack extends NbtModCommand {

        public SetStack() {
            super("setstack", "nbt setstack <amount>", "Allows you to set a current items stack");
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            if (args.length != 1) return true;

            return false;
        }

        @Override
        public Boolean trigger(String[] args) {
            if (this.handleHelp(args)) return true;

            int s;
            try {
                s = Integer.valueOf(args[0]);
            } catch (Exception e) {
                this.displayChatMessage(String.format("%sPlease enter a valid number", ChatUtils.RED));
                return true;
            }

            ItemStack stack = this.heldItem();
            stack.setCount(s);

            ClientUtils.refreshInventory();

            return true;
        }
    }
    public static class Show extends NbtModCommand {

        public Show() {
            super("show", "", "Displays the current nbt data");
        }

        @Override
        public Boolean trigger(String[] args) {
            this.displayChatMessage(this.getItemNbtAsString());
            
            return true;
        }
    }
    public static class NbtCopy extends NbtModCommand {

        public NbtCopy() {
            super("copy", "", "Copies the current NBT to clipboard");
        }

        @Override
        public Boolean trigger(String[] args) {
            MinecraftClient client = ComoClient.getClient();
            client.keyboard.setClipboard(this.getItemNbtAsString());
            
            this.displayChatMessage("Copied to clipboard!");

            return true;
        }
    }
}
