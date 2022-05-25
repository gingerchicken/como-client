package net.como.client.mixin.render;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.events.client.AddMessageEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

@Mixin(ChatHud.class)
public class ChatHudMixin {
	@Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;I)V", cancellable = true)
	private void onAddMessage(Text chatText, int chatLineId, CallbackInfo ci) {
		ComoClient.emitter.triggerEvent(new AddMessageEvent(chatText, chatLineId, ci));
	}
}
