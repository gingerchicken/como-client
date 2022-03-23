package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.events.OnSubmersionTypeEvent;
import net.como.client.events.UpdateCameraEvent;
import net.como.client.interfaces.mixin.ICamera;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

@Mixin(Camera.class)
public abstract class CameraMixin implements ICamera {    
    @Inject(at = @At("RETURN"), method="getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;", cancellable = true)
    public void onGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        ComoClient.emitter.triggerEvent(new OnSubmersionTypeEvent(cir));
    }

    @Inject(at = @At("HEAD"), method="update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", cancellable = true)
    public void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        ComoClient.emitter.triggerEvent(new UpdateCameraEvent(area, focusedEntity, thirdPerson, inverseView, tickDelta, ci));
    }

    @Shadow abstract protected void setPos(Vec3d pos);

    @Override
    public void forceSetPos(Vec3d pos) {
        this.setPos(pos);
    }

    @Shadow private float pitch;
    @Shadow private float yaw;

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}