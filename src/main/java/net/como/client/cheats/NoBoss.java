package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.Cheat;

public class NoBoss extends Cheat {
    public NoBoss() {
        super("NoBoss");
    }

    @SuppressWarnings("unchecked")
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onBossBarHudRender": {
                // Stop the boss bars being rendered
                CallbackInfo ci = (CallbackInfo)args[0];

                ci.cancel();
                break;
            }
            case "onBossBarHudSkyEffects": {
                // Stop the sky and what not being changed.
                CallbackInfoReturnable<Boolean> cir = (CallbackInfoReturnable<Boolean>)args[0];
                cir.setReturnValue(false);
                
                break;
            }
        }
    }
}
