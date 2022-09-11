package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.ComoClient;
import net.como.client.events.client.GetEntitiesEvent;
import net.como.client.interfaces.mixin.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements IClientWorld {
    @Inject(at = @At("HEAD"), method = "getEntities()Ljava/lang/Iterable;", cancellable = true)
    private void onGetEntities(CallbackInfoReturnable<Iterable<Entity>> cir) {
        ComoClient.getInstance().emitter.triggerEvent(new GetEntitiesEvent(cir));
    }

    @Shadow private PendingUpdateManager pendingUpdateManager;

    @Override
    public PendingUpdateManager obtainPendingUpdateManager() {
        return this.pendingUpdateManager;
    }
}
