package net.como.client.commands.nbt;

import java.util.Arrays;
import java.util.List;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.InventoryUtils;
import net.como.client.utils.NbtUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemEggCommand extends Command {

    public ItemEggCommand() {
        super("createitemegg", "createitemegg <item id> <count> <nbt|optional>", "Creates a spawn egg for a given item");
    }

    private ItemStack enumerateStack(String[] args) {
        String rawId = args[0];

        // Get the Item type
        Identifier id;

        // Get the id
        try {
            StringReader reader = new StringReader(rawId);
            id = Identifier.fromCommandInput(reader);
        } catch (CommandSyntaxException e) {
            this.displayChatMessage("%s%s", ChatUtils.RED, e.toString());
            return null;
        }

        // Get the item
        Item item = Registry.ITEM.get(id);

        ItemStack stack = new ItemStack(item);

        // Get the count
        if (args.length > 1) {
            int count;
            try {
                count = Integer.valueOf(args[1]);
            } catch (Exception e) {
                this.displayChatMessage("%s%s", ChatUtils.RED, e.toString());
                return null;
            }

            stack.setCount(count);
        }

        // Get the NBT
        if (args.length > 2) {
            // Get the rest of the args
            String nbt = this.combineArgs(Arrays.copyOfRange(args, 2, args.length));

            // Parse the NBT
            NbtCompound nbtCompound;
            try {
                nbtCompound = NbtUtils.parseNbt(nbt);
            } catch (Exception e) {
                this.displayChatMessage("%s%s", ChatUtils.RED, e.toString());
                return null;
            }

            // Set the NBT
            stack.setNbt(nbtCompound);
        }

        // Create the spawn egg
        return NbtUtils.createItemSpawnEgg(stack, true);
    }

    @Override
    public Boolean trigger(String[] args) {
        ItemStack stack = args.length == 0 ?
            ComoClient.me().getMainHandStack() : this.enumerateStack(args);

        if (stack == null) {
            this.displayChatMessage("%sInvalid item", ChatUtils.RED);
            return true;
        }

        // Create the spawn egg
        ItemStack spawnEgg = NbtUtils.createItemSpawnEgg(stack, true);

        // Give the spawn egg
        NbtUtils.giveItem(spawnEgg, true, InventoryUtils.getFreeSlot());

        // Display the item
        this.displayChatMessage("Spawn egg created for '%s'", stack.getName().getString());

        // Say all is good
        return true;
    }

    @Override
    public List<String> getSuggestions() {
        return BlockUtils.getIds();
    }
}
