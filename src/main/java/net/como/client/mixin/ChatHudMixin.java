package net.como.client.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

@Mixin(ChatHud.class)
public class ChatHudMixin extends DrawableHelper {
	@Inject(at = @At("HEAD"),
		method = "addMessage(Lnet/minecraft/text/Text;I)V",
		cancellable = true)
	private void onAddMessage(Text chatText, int chatLineId, CallbackInfo ci) {
		CheatClient.triggerAllEvent("onAddMessage", new Object[]{(Object)chatText, (Object)chatLineId, (Object)ci});
	}
}
