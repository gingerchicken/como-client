package net.como.client.structures.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class Attribute {
    private final LivingEntity entity;

    /**
     * Get the entity
     * @return LivingEntity
     */
    public LivingEntity getEntity() {
        return entity;
    }

    public int getColour() {
        return 0xFFFFFFFF;
    }

    public Text getText() {
        return Text.of("");
    }

    public Attribute(LivingEntity entity) {
        this.entity = entity;
    }
}
