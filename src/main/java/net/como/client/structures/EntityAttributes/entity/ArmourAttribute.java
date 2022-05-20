package net.como.client.structures.EntityAttributes.entity;

import net.como.client.structures.EntityAttributes.Attribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class ArmourAttribute extends Attribute {

    public ArmourAttribute(LivingEntity entity) {
        super(entity);
    }
    
    @Override
    public Text getText() {
        return Text.of(String.valueOf(this.getEntity().getArmor()));
    }
}
