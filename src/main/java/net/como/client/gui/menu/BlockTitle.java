package net.como.client.gui.menu;

import net.como.client.ComoClient;
import net.como.client.gui.menu.structures.MenuBlockTile;
import net.como.client.structures.Colour;
import net.como.client.utils.Render2DUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public class BlockTitle extends MenuBlockTile {
    public BlockTitle(MenuBlock container, String title) {
        super(container, title, new Colour(255, 255, 255, 100));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Vec2f pos = this.getScreenPosition();
        Vec2f size = this.getSize();

        Render2DUtils.renderBackgroundBox(
            matrices,
            (int)pos.x,
            (int)pos.y,
            (int)(pos.x + size.x),
            (int)(pos.y + size.y + 1),
            this.getBackgroundColour(),
            new Colour(0, 0, 0, 0)
        );

        float offsetX = 2;
        float offsetY = 3;

        Render2DUtils.renderSimpleText(
            matrices,
            this.title,
            (int)(pos.x + offsetX),
            (int)(pos.y + offsetY),
            this.getScaleFactor(),
            textColour
        );
    }
}
