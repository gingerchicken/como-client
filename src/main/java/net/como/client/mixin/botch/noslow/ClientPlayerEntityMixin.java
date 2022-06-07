package net.como.client.mixin.botch.noslow;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.modules.movement.NoSlow;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    private NoSlow getNoSlow() {
        return (NoSlow)(ComoClient.getInstance().getModules().get("noslow"));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0), method = "tickMovement()V")
    public boolean onIsUsingItem(ClientPlayerEntity player) {
        if (!this.getNoSlow().isEnabled()) return player.isUsingItem();

        return false;
    }

    @Override
    public void slowMovement(BlockState state, Vec3d multiplier) {
        if (!this.getNoSlow().isEnabled()) {
            super.slowMovement(state, multiplier);
            return;
        }

        // Do nothing!
    }
}
