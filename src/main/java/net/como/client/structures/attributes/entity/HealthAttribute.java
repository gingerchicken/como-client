package net.como.client.structures.attributes.entity;

import net.como.client.structures.attributes.Attribute;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class HealthAttribute extends Attribute {
    public HealthAttribute(LivingEntity entity) {
        super(entity);
    }

    private int getHealth() {
        return ClientUtils.getHealth(this.getEntity());
    }

    @Override
    public Text getText() {
        return Text.of(
            String.valueOf(this.getHealth())
        );
    }

    @Override
    public int getColour() {
        float f = (this.getHealth() / this.getEntity().getMaxHealth()) * 255 * 2;

        return RenderUtils.RGBA2Int((int)(255*2 - f), (int)(f), 0, 255);
    }
}
