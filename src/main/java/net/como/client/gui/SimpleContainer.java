package net.como.client.gui;

import net.como.client.utils.Render2DUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

public class SimpleContainer extends Widget {

    public SimpleContainer(Vec2f position, Vec2f size) {
        super(position, size);
    }

    @Override
    public void render(MatrixStack matrixStack) {
        Render2DUtils.renderBox(matrixStack,
            (int)this.getPosition().x,
            (int)this.getPosition().y,
            (int)this.getRightPosition().x,
            (int)this.getRightPosition().y
        );
    }

}
