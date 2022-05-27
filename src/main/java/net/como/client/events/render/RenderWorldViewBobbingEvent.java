package net.como.client.events.render;

import net.como.client.events.Event;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderWorldViewBobbingEvent extends Event {
    public GameRenderer gameRenderer;
    public MatrixStack matrixStack;
    public float partalTicks;
    public boolean cancel = false;

    public RenderWorldViewBobbingEvent(GameRenderer gameRenderer, MatrixStack matrixStack, float partalTicks) {
        this.gameRenderer = gameRenderer;
        this.matrixStack = matrixStack;
        this.partalTicks = partalTicks;
    }
}
