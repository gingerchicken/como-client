package net.como.client.structures.EntityAttributes.entity;

import net.como.client.structures.EntityAttributes.Attribute;
import net.como.client.utils.RenderUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class HealthAttribute extends Attribute {
    public HealthAttribute(LivingEntity entity) {
        super(entity);
    }

    private Integer getHealth() {
        return (int)this.getEntity().getHealth();
    }

    @Override
    public Text getText() {
        return Text.of(this.getHealth().toString());
    }

    @Override
    public int getColour() {
        float f = (this.getHealth() / this.getEntity().getMaxHealth()) * 255*2;

        return RenderUtils.RGBA2Int((int)(255*2 - f), (int)(f), 0, 255);
    }
}
