package net.como.client.modules.render;

import net.como.client.modules.Module;

public class HideTitleMessage extends Module {

    public HideTitleMessage() {
        super("HideTitleMessage");

        this.setDescription("Hides the thank you message on the title screen.");

        this.setCategory("Render");
    }
    
    @Override
    public boolean shouldDisplayInModList() {
        return false;
    }
}
