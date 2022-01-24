package net.como.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.GetClientModNameEvent;
import net.minecraft.client.ClientBrandRetriever;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    @Inject(at = @At("RETURN"), method="getClientModName()Ljava/lang/String;", cancellable = true)
    private static void onGetClientModName(CallbackInfoReturnable<String> cir) {
        ComoClient.emitter.triggerEvent(new GetClientModNameEvent(cir));
    }
}
