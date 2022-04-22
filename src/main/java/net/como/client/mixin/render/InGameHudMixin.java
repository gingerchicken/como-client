package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.RenderHealthBarEvent;
import net.como.client.events.RenderPortalOverlayEvent;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = {"render(Lnet/minecraft/client/util/math/MatrixStack;F)V"}, at = {@At("HEAD")})
    public void render(MatrixStack mStack, float tickDelta, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new InGameHudRenderEvent(mStack, tickDelta, ci));
    }

    @Inject(at = @At("HEAD"), method = {"renderPortalOverlay(F)V"}, cancellable = true)
    public void onRenderPortalOverlay(float nauseaStrength, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new RenderPortalOverlayEvent(nauseaStrength, ci));
    }

    @Inject(at = @At("HEAD"), method = "renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V", cancellable = true)
    public void onRenderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(
            new RenderHealthBarEvent(matrices, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking, ci)
        );
    }
}
