package net.como.client.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.como.client.events.RenderTooltipEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(at = @At("HEAD"), method="renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", cancellable = true)
    private void onRenderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new RenderTooltipEvent(matrices, stack, x, y, ci));
    }
}
