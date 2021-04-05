package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.minecraft.client.util.math.MatrixStack;

public class NoWeather extends Cheat {
    public NoWeather() {
        super("Anti-British");
    }

    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderWeather": {
                CallbackInfo ci = (CallbackInfo)args[5];
                ci.cancel();

                break;
            }
            case "onTickRainSplashing": {
                CallbackInfo ci = (CallbackInfo)args[1];
                ci.cancel();

                break;
            }
        }
    }
}
