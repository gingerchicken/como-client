package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;

public class PlayerChatEvent extends Event {
    public CallbackInfo ci;
    public String message;

    public PlayerChatEvent(String message, CallbackInfo ci) {
        this.ci = ci;
        this.message = message;
    }
}
