package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.text.Text;

public class OnDisconnectedEvent extends Event {
    public Text reason;
    public CallbackInfo ci;

    public OnDisconnectedEvent(Text reason, CallbackInfo ci) {
        this.reason = reason;
        this.ci = ci;
    }
}
