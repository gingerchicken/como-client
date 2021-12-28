package net.como.client.gui.menu;

import net.como.client.gui.Widget;
import net.como.client.structures.Colour;
import net.como.client.utils.Render2DUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

public class MenuBlock extends Widget {
    public Colour bgColour, outline;
    
    public static int tileSizes     = 14;
    public static int tileSpacing   = 1;
    public static int tilePadding   = 2;

    public static int calculateHeight(int totalMods) {
        return totalMods * tileSpacing + tileSizes * totalMods;
    }
    
    // TODO add scroll bars

    public MenuBlock(Vec2f position, Vec2f size) {
        super(position, size);
    
        this.bgColour = new Colour(0, 0, 0, 150);
        this.outline  = new Colour(0, 0, 0, 255);
    }

    @Override
    public void render(MatrixStack matrixStack) {
        Vec2f pos = Render2DUtils.relPosition(this.getScreenPosition());
        Vec2f size = Render2DUtils.relPosition(this.getSize());
        
        Render2DUtils.renderBackgroundBox(
            matrixStack,
            (int)pos.x,
            (int)pos.y,
            (int)(pos.x + size.x),
            (int)(pos.y + size.y - 1), // -1 cus something messed up
            bgColour,
            outline
        );

        for (Widget child : this.getChildren()) {
            child.render(matrixStack);
        }
    }

}
