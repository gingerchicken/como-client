package net.como.client.modules.combat;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.InteractionUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoTotem extends Module {

    public AutoTotem() {
        super("AutoTotem");

        this.description = "Automatically places a totem into your off hand";
    }
    
    @Override
    public String listOption() {
        int total = 0;
        
        total += this.getAllTotemSlots().size();
        total += this.hasTotemEquip() ? 1 : 0;

        return Integer.toString(total);
    }

    private List<Integer> getAllTotemSlots() {
        return this.getTotemSlots(ComoClient.me().getInventory().size());
    }

    private List<Integer> getTotemSlots(int total) {
        List<Integer> totems = new ArrayList<Integer>();
        
        PlayerInventory inv = ComoClient.me().getInventory();

        // Iterate over all items that are not armour, crafting, off-hand or hotbar.
        for (int i = 9; i < PlayerInventory.MAIN_SIZE && total > 0; i++) {
            if (this.isTotem(inv.main.get(i))) {
                total--;
                totems.add(i);
            }
        }

        // Check the hotbar
        for (int i = 0; i < 9 && total > 0; i++) {
            if (this.isTotem(inv.main.get(i))) {
                total--;
                totems.add(i + 36);
            }
        }

        return totems;
    }

    private boolean isTotem(ItemStack item) {
        return item.isOf(Items.TOTEM_OF_UNDYING);
    }

    private ItemStack getOffHandStack() {
        return ComoClient.me().getStackInHand(Hand.OFF_HAND);
    }

    private boolean hasTotemEquip() {
        ItemStack item = this.getOffHandStack();

        return this.isTotem(item);
    }

    private int getTotemSlot() {
        List<Integer> slots = this.getTotemSlots(1);

        return slots.size() == 0 ? -1 : slots.get(0);
    }

    private void equipTotem(int origin) {
        // Check if it was empty before since if we move something into it, it won't be empty anymore.
        boolean wasHandEmpty = this.getOffHandStack().isEmpty();

        // Click the totem's slot
        InteractionUtils.pickupItem(origin);

        // Click off hand slot (this may pick up an item that was already in there.)
        InteractionUtils.pickupItem(45);

        if (!wasHandEmpty) {
            // So if it wasn't empty, we will now have the item that was previously in the offhand now at our finger
            // in that case, we place it where the totem used to be and call it a day.
            InteractionUtils.pickupItem(origin);
        };
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                // If we have a totem already we don't need to bother.
                if (this.hasTotemEquip()) break;

                // Make sure we are not in a storage container, slot numbers are a little spicy here.
                if (ClientUtils.isInStorage()) break;

                int totemSlot = this.getTotemSlot();

                // If we don't have a totem, go away.
                if (totemSlot == -1) break;

                // Equip the totem if we don't have any.
                this.equipTotem(totemSlot);

                break;
            }
        }
    }
}
