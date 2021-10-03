package net.como.client.mixin.botch;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class ClientInit {
    @Inject(at = @At("TAIL"), method = "<init>", cancellable = false)
    public void init(CallbackInfo ci) {
        CheatClient.initialise();
    }
}
