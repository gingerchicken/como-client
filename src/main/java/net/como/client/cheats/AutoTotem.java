package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.InteractionUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoTotem extends Cheat {

    public AutoTotem() {
        super("AutoTotem");
    }
    
    private boolean isTotem(ItemStack item) {
        return item.isOf(Items.TOTEM_OF_UNDYING);
    }

    private ItemStack getOffHandStack() {
        return CheatClient.me().getStackInHand(Hand.OFF_HAND);
    }

    private boolean hasTotemEquip() {
        ItemStack item = this.getOffHandStack();

        return this.isTotem(item);
    }

    private int getTotemSlot() {
        PlayerInventory inv = CheatClient.me().getInventory();
        
        // Iterate over all items that are not armour, crafting, off-hand or hotbar.
        for (int i = 9; i < PlayerInventory.MAIN_SIZE; i++) {
            if (this.isTotem(inv.main.get(i))) return i;
        }

        // Check the hotbar
        for (int i = 0; i < 9; i++) {
            if (this.isTotem(inv.main.get(i))) return i + 36;
        }

        return -1;
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
