package net.como.client.modules.render;

import net.como.client.structures.Module;

public class HideTitleMessage extends Module {

    public HideTitleMessage() {
        super("HideTitleMessage");

        this.description = "Hides the thank you message on the title screen.";

        this.setCategory("Render");
    }
    
    @Override
    public boolean shouldDisplayInModList() {
        return false;
    }
}
