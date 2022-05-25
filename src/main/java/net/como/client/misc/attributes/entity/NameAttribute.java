package net.como.client.misc.attributes.entity;

import net.como.client.misc.attributes.Attribute;
import net.como.client.misc.attributes.PlayerAttribute;
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
