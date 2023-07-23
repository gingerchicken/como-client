package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class RenderTooltipEvent extends Event {
    public DrawContext context;
    public ItemStack stack; 
    public int x, y;
    public CallbackInfo ci;

    public RenderTooltipEvent(DrawContext context, ItemStack stack, int x, int y, CallbackInfo ci) {
        this.context = context;
        this.stack = stack;
        this.x = x;
        this.y = y;
        this.ci = ci;
    }
}
