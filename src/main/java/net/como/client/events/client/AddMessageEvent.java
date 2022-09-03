package net.como.client.events.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.text.Text;

public class AddMessageEvent extends Event {
    public Text chatText;
    public CallbackInfo ci;

    public AddMessageEvent(Text chatText, CallbackInfo ci) {
        this.chatText = chatText;
        this.ci = ci;
    }
}
