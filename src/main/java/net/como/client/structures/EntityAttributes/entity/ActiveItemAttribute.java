package net.como.client.structures.EntityAttributes.entity;

import net.como.client.structures.EntityAttributes.Attribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class ActiveItemAttribute extends Attribute {
    public ActiveItemAttribute(LivingEntity entity) {
        super(entity);
    }

    @Override
    public Text getText() {
        return this.getEntity().getMainHandStack().getName();
    }
}
