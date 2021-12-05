package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.DisconnectEvent;
import net.como.client.events.OnClientCloseEvent;
import net.como.client.interfaces.mixin.IClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.gui.screen.Screen;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IClient {
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
}
