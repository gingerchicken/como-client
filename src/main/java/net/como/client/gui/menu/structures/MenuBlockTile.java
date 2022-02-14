package net.como.client.gui.menu.structures;

import net.como.client.gui.Widget;
import net.como.client.gui.menu.MenuBlock;
import net.como.client.structures.Colour;
import net.como.client.utils.Render2DUtils;
import net.minecraft.client.util.math.MatrixStack;
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

    public Colour getBackgroundColour(Float delta) {
        return this.getBackgroundColour();
    }

    public Colour getOutlineColour(Float delta) {
        Colour c = this.getBackgroundColour(delta);

        return new Colour(c.r, c.g, c.b, c.a - 50);
    }

    public Colour getOutlineColour() {
        return this.getOutlineColour(0f);
    }

    public MenuBlockTile(MenuBlock container, String title, Colour backgroundColour) {
        super(
            new Vec2f(0, MenuBlock.calculateHeight(container.getChildren().size())),
            new Vec2f(0, 0)
        );

        this.index = container.getChildren().size();

        this.title = title;
        this.setParent(container);

        this.textColour = new Colour(255, 255, 255, 255);
        this.bgColour   = backgroundColour;
        
        this.clickable = true;
    }

    private MenuBlock getParentMenuBlock() {
        Widget widgetParent = this.getParent();

        if (!(widgetParent instanceof MenuBlock)) return null;

        return (MenuBlock)(widgetParent);
    }

    @Override
    public Vec2f getPosition() {
        return new Vec2f(0, MenuBlock.calculateHeight(this.index) * this.getScaleFactor());
    }

    @Override
    public Vec2f getSize() {
        return new Vec2f(
            this.getParentMenuBlock().getSize().x,
            MenuBlock.tileSizes * this.getScaleFactor()
        );
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
            (int)(pos.y + size.y),
            this.getBackgroundColour(delta),
            this.getOutlineColour()
        );

        // Text offsets
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
