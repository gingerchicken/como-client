package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public class UpdateCameraEvent extends Event {
    public CallbackInfo ci;
    public BlockView area;
    public Entity focusedEntity;
    public boolean thirdPerson;
    public boolean inverseView;
    public float tickDelta;

    public UpdateCameraEvent(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
        this.inverseView = inverseView;
        this.tickDelta = tickDelta;
        this.ci = ci;
    }
}
