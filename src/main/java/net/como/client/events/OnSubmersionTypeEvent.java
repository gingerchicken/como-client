package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;
import net.minecraft.client.render.CameraSubmersionType;

public class OnSubmersionTypeEvent extends Event {
    public CallbackInfoReturnable<CameraSubmersionType> cir;

    public OnSubmersionTypeEvent(CallbackInfoReturnable<CameraSubmersionType> cir) {
        this.cir = cir;
    }
}
