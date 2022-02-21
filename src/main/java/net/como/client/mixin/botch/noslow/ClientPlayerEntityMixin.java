package net.como.client.mixin.botch.noslow;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.modules.movement.NoSlow;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    private NoSlow getNoSlow() {
        return (NoSlow)(ComoClient.Modules.get("noslow"));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0), method = "tickMovement()V")
    public boolean onIsUsingItem(ClientPlayerEntity player) {
        if (!this.getNoSlow().isEnabled()) return player.isUsingItem();

        return false;
    }
}
