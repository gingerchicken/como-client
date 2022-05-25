package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.entity.player.PlayerEntity;

public class StopUsingItemEvent extends Event {
    public PlayerEntity playerEntity;
    public CallbackInfo ci;
    
    public StopUsingItemEvent(PlayerEntity playerEntity, CallbackInfo ci) {
        this.playerEntity = playerEntity;
        this.ci = ci;
    }
}
