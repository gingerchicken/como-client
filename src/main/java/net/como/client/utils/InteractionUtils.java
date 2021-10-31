package net.como.client.utils;

import net.como.client.CheatClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.slot.SlotActionType;

public class InteractionUtils {
    public static ClientPlayerInteractionManager getManager() {        
        return CheatClient.getClient().interactionManager;
    }

    public static void pickupItem(int slot) {
        getManager().clickSlot(0, slot, 0, SlotActionType.PICKUP, CheatClient.me());
    }
}
