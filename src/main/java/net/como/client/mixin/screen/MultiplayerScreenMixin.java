package net.como.client.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.screen.ConnectEvent;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.network.ServerInfo;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @Inject(at = @At("HEAD"), method = "connect(Lnet/minecraft/client/network/ServerInfo;)V")
	private void onConnect(ServerInfo entry, CallbackInfo ci) {
        // For the util
        ServerUtils.setLastServer(entry);

        // For the event
		ComoClient.emitter.triggerEvent(new ConnectEvent(entry, ci));
        
	}
}
