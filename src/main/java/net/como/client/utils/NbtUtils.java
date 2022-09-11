package net.como.client.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.como.client.ComoClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

public class NbtUtils {
    public static NbtCompound enchantment(String enchantmentName, Short level) {
        NbtCompound enchant = new NbtCompound();

        enchant.put("id", NbtString.of(enchantmentName));
        enchant.put("lvl", NbtShort.of(level));

        return enchant;
    }

    public static void giveItem(ItemStack item) {
        giveItem(item, true);
    }

    public static void giveItem(ItemStack item, boolean refreshInv) {
        giveItem(item, refreshInv, InventoryUtils.getMainHandSlot());
    }

    public static void giveItem(ItemStack item, boolean refreshInv, int slot) {
        // Send the packet
        CreativeInventoryActionC2SPacket packet = new CreativeInventoryActionC2SPacket(slot, item);
        ComoClient.getClient().getNetworkHandler().sendPacket(packet);

        if (refreshInv) ClientUtils.refreshInventory();
    }

    public static NbtCompound nbtFromString(String str) throws CommandSyntaxException {
        return StringNbtReader.parse(str);
    }

    public static ItemStack createItemSpawnEgg(ItemStack targetStack, boolean customName) {
        ItemStack itemStack = new ItemStack(Items.BAT_SPAWN_EGG);
    
        // Create the item entity nbt
        NbtCompound entityNbt = new NbtCompound();
        entityNbt.putString("id", "minecraft:item");

        // Get the item nbt
        NbtCompound itemNbt = new NbtCompound();
        itemNbt.putString("id", Registry.ITEM.getId(targetStack.getItem()).toString());
        itemNbt.putInt("Count", targetStack.getCount());
        if (targetStack.hasNbt()) itemNbt.put("tag", targetStack.getNbt());
        
        // Add the item nbt to the entity nbt
        entityNbt.put("Item", itemNbt);

        // Create the spawn data nbt
        NbtCompound spawnEggNbt = new NbtCompound();
        spawnEggNbt.put("EntityTag", entityNbt);

        // Add the spawn data to the item nbt
        itemStack.setNbt(spawnEggNbt);

        // Set a custom name
        if (!customName) return itemStack;

        // Set custom name
        itemStack.setCustomName(
            Text.of(ChatUtils.GREEN + "Create " + ChatUtils.WHITE + targetStack.getName().getString())
        );

        // Return the item
        return itemStack;
    }

    public static NbtCompound parseNbt(String rawNbt) throws CommandSyntaxException {
        return StringNbtReader.parse(rawNbt);
    }

    public static final String[] ENCHANTMENTS = {"minecraft:protection","minecraft:fire_protection","minecraft:feather_falling","minecraft:blast_protection","minecraft:projectile_protection","minecraft:respiration","minecraft:aqua_affinity","minecraft:thorns","minecraft:depth_strider","minecraft:sharpness","minecraft:smite","minecraft:bane_of_arthropods","minecraft:knockback","minecraft:fire_aspect","minecraft:looting","minecraft:efficiency","minecraft:silk_touch","minecraft:unbreaking","minecraft:fortune","minecraft:power","minecraft:punch","minecraft:flame","minecraft:infinity","minecraft:luck_of_the_sea","minecraft:lure"};
}
