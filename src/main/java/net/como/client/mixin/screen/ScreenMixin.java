package net.como.client.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.render.RenderTooltipEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

@Mixin(Screen.class)
public class ScreenMixin {
    // TODO appendTooltip might be a better target
    // @Inject(at = @At("HEAD"), method="renderTooltip(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemStack;II)V", cancellable = true)
    // private void onRenderTooltip(DrawContext context, ItemStack stack, int x, int y, CallbackInfo ci) {
    //     ComoClient.getInstance().emitter.triggerEvent(new RenderTooltipEvent(context, stack, x, y, ci));
    // }
}
