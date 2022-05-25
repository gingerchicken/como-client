package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.client.DisconnectEvent;
import net.como.client.events.client.OnClientCloseEvent;
import net.como.client.interfaces.mixin.IClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.gui.screen.Screen;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IClient {
    @Inject(at = @At("HEAD"), method = "close()V", cancellable = false)
    private void onClose(CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new OnClientCloseEvent(ci));

        // Close our client after all is said and done.
        ComoClient.close();
    }

    @Shadow
    @Final
    FontManager fontManager;

    @Override
    public FontManager getFontManager() {
        return fontManager;
    }

    @Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", cancellable = true)
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new DisconnectEvent(ci));
    }

    @Shadow protected abstract void doItemUse();

    public void performItemUse() {
        this.doItemUse();
    }

    @Shadow protected abstract boolean doAttack();

    @Override
    public boolean performAttack() {
        return this.doAttack();
    }


    @Shadow protected int attackCooldown;
    @Override
    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = cooldown;
    }
}
