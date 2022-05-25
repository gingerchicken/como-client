package net.como.client.structures.attributes.entity;

import net.como.client.structures.attributes.Attribute;
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
