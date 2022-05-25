package net.como.client.structures.attributes;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerAttribute extends Attribute {
    public PlayerEntity getPlayer() {
        return (PlayerEntity)this.getEntity();
    }

    public PlayerAttribute(PlayerEntity player) {
        super(player);
    }
    
}
