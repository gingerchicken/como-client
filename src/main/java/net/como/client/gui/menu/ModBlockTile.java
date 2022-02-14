package net.como.client.gui.menu;

import net.como.client.gui.menu.structures.MenuBlockTile;
import net.como.client.structures.Colour;
import net.como.client.structures.Module;

public class ModBlockTile extends MenuBlockTile {
    private Module module;
    public Module getModule() {
        return this.module;
    }

    @Override
    public Colour getBackgroundColour() {
        return new Colour(0, green, 0 , 150);
    }

    @Override
    public Colour getBackgroundColour(Float delta) {
        Colour c = this.getBackgroundColour();

        Float k = module.isEnabled() ? 1f : -1f;
        if ((module.isEnabled() && green >= greenMax) || (!module.isEnabled() && green <= 0)) k = 0f;

        return new Colour(
            c.r, c.g + greenStep * delta * k, c.b, c.a
        );
    }

    private float green = 0.0f;
    private float greenStep = 50f;
    private float greenMax = 255f;

    public ModBlockTile(MenuBlock container, Module module) {
        super(container, module.getName(), new Colour(0, module.isEnabled() ? 255f : 0, 0, 150));

        this.green = module.isEnabled() ? 255f : 0;

        this.module = module;
    }
    
    @Override
    public void clicked() {
        this.module.toggle();
    }

    @Override
    public void tick() {
        super.tick();
        Boolean e = this.getModule().isEnabled();

        if (!e) {
            if (green > 0) green -= greenStep;
            else green = 0f;
            return;
        }

        if (green < greenMax) green += greenStep;
        else green = greenMax;
    }
}
