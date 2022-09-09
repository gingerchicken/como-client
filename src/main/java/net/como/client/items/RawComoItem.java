package net.como.client.items;

import java.util.Base64;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.utils.NbtUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RawComoItem implements CreativeItem {
    private String itemType;
    private String encodedNbt;

    public RawComoItem(String itemType, String encodedNbt) {
        this.itemType = itemType;
        this.encodedNbt = encodedNbt;
    }

    /**
     * Gets the Minecraft item type.
     * @return The item type.
     */
    public Item getItem() {
        // Get a string reader for the item string
        StringReader reader = new StringReader(this.itemType);
                
        try {
            // Get the id
            Identifier id = Identifier.fromCommandInput(reader);

            // Get the item
            return Registry.ITEM.get(id);
        } catch (Exception e) {
            return null;
        }
    }

    public String getNbtString() {
        // The data is encoded as a base64 string
        String nbt = new String(Base64.getDecoder().decode(this.encodedNbt));

        // Return the decoded string
        return nbt;
    }

    public NbtCompound getNbt() throws CommandSyntaxException {
        // Get the nbt string
        String nbt = this.getNbtString();

        // Convert the string to an NBT compound
        return NbtUtils.nbtFromString(nbt);
    }

    public ItemStack getStack() {
        // Get the item
        Item item = this.getItem();

        // Get the nbt
        NbtCompound nbt = null;
        try {
            nbt = this.getNbt();
        } catch (Exception e) {
            return null;
        }

        // Create the stack
        ItemStack stack = new ItemStack(item);

        // Set the nbt
        stack.setNbt(nbt);

        // Return the stack
        return stack;
    }

    // Creative Item Overrides

    @Override
    public boolean useName() {
        return false;
    }

    @Override
    public ItemStack readyStack() {
        return this.getStack();
    }

    @Override
    public String getName() {
        return this.getStack().getName().getString();
    }
}
