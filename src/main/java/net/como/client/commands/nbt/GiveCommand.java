package net.como.client.commands.nbt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.commands.structures.Command;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.NbtUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GiveCommand extends Command {

    public GiveCommand() {
        super("give", "give <item> <amount> <nbt>", "Client-side give command");
    }
    
    @Override
    public boolean shouldShowHelp(String[] args) {
        return args.length == 0;
    }

    @Override
    public List<String> getSuggestions() {
        return BlockUtils.getIds();
    }

    @Override
    public Boolean trigger(String[] args) {
        if (this.handleHelp(args)) return true;

        String idStr = args[0];
        Identifier id;

        // Get the id
        try {
            StringReader reader = new StringReader(idStr);
            id = Identifier.fromCommandInput(reader);
        } catch (CommandSyntaxException e) {
            this.displayChatMessage("%s%s", ChatUtils.RED, e.toString());
            return true;
        }

        ItemStack stack = new ItemStack(Registry.ITEM.get(id));

        // Get item count
        if (args.length > 1) {
            Integer count;
            try {
                count = Integer.valueOf(args[1]);
            } catch (Exception e) {
                this.displayChatMessage("%s%s", ChatUtils.RED, e.toString());
                return true;
            }

            stack.setCount(count);
        }

        // Get nbt data
        if (args.length > 2) {
            NbtCompound nbt;
            String nbtStr;

            String[] rArgs = Arrays.copyOfRange(args, 2, args.length);
            nbtStr = this.combineArgs(rArgs);

            try {
                nbt = NbtUtils.nbtFromString(nbtStr);
            } catch (Exception e) {
                this.displayChatMessage("%s%s", ChatUtils.RED, e.toString());
                return true;
            }

            stack.setNbt(nbt);
        }

        NbtUtils.giveItem(stack);
        return true;
    }
}
