package net.como.client.mixin.botch.shulkerdupe;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.modules.dupes.ShulkerDupe;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;


@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    // The shulker dupe needs to be tick perfect hence we are directly hooking it.
    @Inject(at = @At("TAIL"), method="send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    public void onSend(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
        ShulkerDupe shulkerDupe = (ShulkerDupe)(ComoClient.getInstance().getModules().get("shulkerdupe"));

        if (shulkerDupe.isEnabled()) shulkerDupe.handlePacket(packet);
    }
}
