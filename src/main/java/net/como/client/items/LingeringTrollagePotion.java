package net.como.client.items;

import net.minecraft.item.Items;

public class LingeringTrollagePotion extends TrollagePotion {
    public LingeringTrollagePotion() {
        super();
        this.itemType = Items.LINGERING_POTION;
    }

    @Override
    public String getName() {
        return "Lingering Trollage Potion";
    }
}
