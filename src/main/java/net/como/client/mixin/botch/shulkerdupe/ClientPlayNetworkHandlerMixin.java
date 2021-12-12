package net.como.client.mixin.botch.shulkerdupe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.modules.ShulkerDupe;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;


@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    // The shulker dupe needs to be tick perfect hence we are directly hooking it.
    @Inject(at = @At("TAIL"), method="sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        ShulkerDupe shulkerDupe = (ShulkerDupe)(ComoClient.Modules.get("shulkerdupe"));

        if (shulkerDupe.isEnabled()) shulkerDupe.handlePacket(packet);
    }
}
