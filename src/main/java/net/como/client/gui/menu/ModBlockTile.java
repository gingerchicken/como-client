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
        return this.getModule().isEnabled()
        ?   activatedColour
        :   deactivatedColour;
    }

    private static Colour activatedColour    = new Colour(0, 255, 0, 150);
    private static Colour deactivatedColour  = new Colour(0, 0, 0, 150);

    public ModBlockTile(MenuBlock container, Module module) {
        super(container, module.getName(), activatedColour);

        this.module = module;
    }
    
}
