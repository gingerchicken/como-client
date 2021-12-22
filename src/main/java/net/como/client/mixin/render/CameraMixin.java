package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.OnSubmersionTypeEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;

@Mixin(Camera.class)
public class CameraMixin {    
    @Inject(at = @At("RETURN"), method="getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;", cancellable = true)
    public void onGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        ComoClient.emitter.triggerEvent(new OnSubmersionTypeEvent(cir));
    }
}