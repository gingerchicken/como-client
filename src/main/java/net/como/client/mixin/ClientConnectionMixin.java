package net.como.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.como.client.events.DisconnectEvent;
import net.minecraft.network.ClientConnection;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "handleDisconnection()V", cancellable = false)
    public void handleDisconnection(CallbackInfo ci) {
        CheatClient.emitter.triggerEvent(new DisconnectEvent(ci));
    }
}
