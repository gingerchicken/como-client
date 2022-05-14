package net.como.client.gui;

import net.minecraft.client.gui.Drawable;

public interface Widget extends Drawable {
    public void tick();
    public void init();
    public void close();
}
