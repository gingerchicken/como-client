package net.como.client.mixin.botch;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.CheatClient;
import net.como.client.cheats.Timer;
import net.como.client.utils.ClientUtils;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(RenderTickCounter.class)
public class TimerMixin {
    @Shadow
	private float lastFrameDuration;
	
	@Inject(at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J",
            opcode = Opcodes.PUTFIELD,
            ordinal = 0
        ),
        method = {"beginRenderTick(J)I"}
    )
	public void onBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        // Make sure that we are playing
        if (!ClientUtils.inGame()) return;

        Timer timer = (Timer)CheatClient.Cheats.get("timer");
        if (!timer.isEnabled()) return;

		lastFrameDuration *= (double)timer.getSetting("Speed").value;
	}
}
