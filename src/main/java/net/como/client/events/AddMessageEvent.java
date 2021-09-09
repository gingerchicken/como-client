package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.text.Text;

public class AddMessageEvent extends Event {
    public Text chatText;
    public int chatLineId;
    public CallbackInfo ci;

    public AddMessageEvent(Text chatText, int chatLineId, CallbackInfo ci) {
        this.chatText = chatText;
        this.chatLineId = chatLineId;
        this.ci = ci;
    }
}
