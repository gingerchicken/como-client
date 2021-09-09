package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;

public class PlayerChatEvent extends Event {
    public CallbackInfo ci;
    public String message;

    public PlayerChatEvent(String message, CallbackInfo ci) {
        this.ci = ci;
        this.message = message;
    }
}
