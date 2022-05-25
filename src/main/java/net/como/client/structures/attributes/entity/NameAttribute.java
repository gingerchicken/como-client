package net.como.client.structures.attributes.entity;

import net.como.client.structures.attributes.Attribute;
import net.como.client.structures.attributes.PlayerAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class NameAttribute extends Attribute {

    public NameAttribute(LivingEntity entity) {
        super(entity);
    }

    @Override
    public Text getText() {
        return this.getEntity().getName();
    }    
}
