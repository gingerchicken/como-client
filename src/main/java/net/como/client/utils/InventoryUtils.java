package net.como.client.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.como.client.ComoClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    public static List<Integer> getItemSlots(int total, Predicate<ItemStack> isItem) {
        List<Integer> slots = new ArrayList<Integer>();
        
        PlayerInventory inv = ComoClient.me().getInventory();

        // Iterate over all items that are not armour, crafting, off-hand or hotbar.
        for (int i = 9; i < PlayerInventory.MAIN_SIZE && total > 0; i++) {
            if (isItem.test(inv.main.get(i))) {
                total--;
                slots.add(i);
            }
        }

        // Check the hotbar
        for (int i = 0; i < 9 && total > 0; i++) {
            if (isItem.test(inv.main.get(i))) {
                total--;
                slots.add(i + 36);
            }
        }

        return slots;
    }

    public static int getSlotWithItem(Item i) {
        List<Integer> slots = getItemSlots(1, item -> item.isOf(i));

        if (slots.size() == 0) return -1;

        return slots.get(0);
    }

    public static void moveItem(int from, int to) {
        if (from == to) return;

        // Check if it was empty before since if we move something into it, it won't be empty anymore.
        boolean wasEmpty = ComoClient.me().getInventory().getStack(to).isEmpty();

        // Click the slot
        InteractionUtils.pickupItem(from);

        // Click off 'to' slot (this may pick up an item that was already in there.)
        InteractionUtils.pickupItem(to);

        if (!wasEmpty) {
            // So if it wasn't empty, we will now have the item that was previously in the 'to' slot now at our finger
            // in that case, we place it where the totem used to be and call it a day.
            InteractionUtils.pickupItem(from);
        }
    }

    public static int getMainHandSlot() {
        ClientPlayerEntity me = ComoClient.me();
        PlayerInventory inv = me.getInventory();

        return inv.selectedSlot + 36;
    }
}
