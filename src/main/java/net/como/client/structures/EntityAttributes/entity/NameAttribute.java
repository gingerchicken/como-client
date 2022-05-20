package net.como.client.structures.EntityAttributes.entity;

import net.como.client.structures.EntityAttributes.Attribute;
import net.como.client.structures.EntityAttributes.PlayerAttribute;
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
