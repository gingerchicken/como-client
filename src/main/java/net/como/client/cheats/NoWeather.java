package net.como.client.cheats;

import javax.security.auth.callback.Callback;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;

public class NoWeather extends Cheat {
    public NoWeather() {
        super("AntiBritish");

        this.description = "Hides the rain.";
    }

    public void receiveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderWeather": {
                CallbackInfo ci = (CallbackInfo)args[5];
                
                // Hide the stuff falling from the sky.
                ci.cancel();

                break;
            }
            case "onTickRainSplashing": {
                CallbackInfo ci = (CallbackInfo)args[1];
                
                // Hide the splashes on the floor and the horrible ambient noise of living in the UK.
                ci.cancel();

                break;
            }
        }
    }
}
