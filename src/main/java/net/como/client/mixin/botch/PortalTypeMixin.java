package net.como.client.mixin.botch;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.modules.NoPortal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class PortalTypeMixin {
    private Screen tempCurrentScreen;
    
    @Shadow
    @Final
    protected MinecraftClient client;

    @Inject(at = @At(value = "FIELD",
		target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
		opcode = Opcodes.GETFIELD,
		ordinal = 0), method = {"updateNausea()V"}, cancellable = true)
	private void beforeUpdateNausea(CallbackInfo ci) {
        NoPortal noPortal = (NoPortal)ComoClient.Cheats.get("noportal");
        if (!noPortal.isEnabled() || !(boolean)noPortal.getSetting("AllowTyping").value) return;

        tempCurrentScreen = client.currentScreen;
        client.currentScreen = null;
	}

    @Inject(at = @At(value = "FIELD",
		target = "Lnet/minecraft/client/network/ClientPlayerEntity;nextNauseaStrength:F",
		opcode = Opcodes.GETFIELD,
		ordinal = 1), method = {"updateNausea()V"})
	private void afterUpdateNausea(CallbackInfo ci) {
		if(tempCurrentScreen == null)
			return;
		
		client.currentScreen = tempCurrentScreen;
		tempCurrentScreen    = null;
	}
}
