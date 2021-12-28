package net.como.client.gui.menu.structures;

import net.como.client.ComoClient;
import net.como.client.gui.Widget;
import net.como.client.gui.menu.MenuBlock;
import net.como.client.structures.Colour;
import net.como.client.utils.Render2DUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public class MenuBlockTile extends Widget {
    private Colour bgColour; // This may vary

    // TODO make a "quad" object or something, I use this in a load of widgets.
    public Colour outlineColour = new Colour(255, 255, 255, 150);

    public Colour textColour;
    public String title;
    public int index;

    public Colour getBackgroundColour() {
        return this.bgColour;
    }

    public MenuBlockTile(MenuBlock container, String title, Colour backgroundColour) {
        super(
            new Vec2f(0, MenuBlock.calculateHeight(container.getChildren().size())),
            new Vec2f(container.getSize().x, (float)MenuBlock.tileSizes)
        );

        this.index = container.getChildren().size();

        this.title = title;
        this.setParent(container);

        this.textColour = new Colour(255, 255, 255, 255);
        this.bgColour   = backgroundColour; // new Colour(255, 255, 255, 150);
    }

    private MenuBlock getParentMenuBlock() {
        Widget widgetParent = this.getParent();

        if (!(widgetParent instanceof MenuBlock)) return null;

        return (MenuBlock)(widgetParent);
    }

    @Override
    public Vec2f getPosition() {
        return new Vec2f(0, MenuBlock.calculateHeight(this.index));
    }

    @Override
    public Vec2f getSize() {
        return new Vec2f(
            this.getParentMenuBlock().getSize().x,
            MenuBlock.tileSizes
        );
    }
    
    @Override
    public void render(MatrixStack matrixStack) {
        Vec2f pos = this.getScreenPosition();
        Vec2f size = this.getSize();

        Render2DUtils.renderBackgroundBox(
            matrixStack,
            (int)pos.x + 1,
            (int)pos.y,
            (int)(pos.x + size.x - 1),
            (int)(pos.y + size.y),
            this.getBackgroundColour(),
            this.outlineColour
        );

        float offsetX = 2;
        float offsetY = 3;

        ComoClient.textRenderer.drawWithShadow(
            matrixStack,
            Text.of(this.title),
            pos.x + offsetX,
            pos.y + offsetY,
            RenderUtils.RGBA2Int(textColour)
        );
    }
}
