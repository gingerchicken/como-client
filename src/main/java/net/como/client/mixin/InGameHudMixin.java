package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = {"render(Lnet/minecraft/client/util/math/MatrixStack;F)V"}, at = {@At("HEAD")})
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        CheatClient.triggerAllEvent("InGameHubRender", new Object[]{matrixStack, tickDelta, ci});
    }
}
