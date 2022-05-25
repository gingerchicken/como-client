package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.client.DisconnectEvent;
import net.como.client.events.client.HandleDisconnectionEvent;
import net.minecraft.network.ClientConnection;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "handleDisconnection()V", cancellable = true)
    public void handleDisconnection(CallbackInfo ci) {
        ComoClient.getInstance().emitter.triggerEvent(new DisconnectEvent(ci));
        ComoClient.getInstance().emitter.triggerEvent(new HandleDisconnectionEvent(ci));
    }
}
