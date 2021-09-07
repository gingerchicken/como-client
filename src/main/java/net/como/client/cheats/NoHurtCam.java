package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;

public class NoHurtCam extends Cheat {
    public NoHurtCam() {
        super("NoHurtCam");

        this.description = "Disables the screen rotation when getting damaged.";
    }

    public void receiveEvent(String event, Object[] args) {
        switch (event) {
            case "onBobViewWhenHurt": {
                CallbackInfo ci = (CallbackInfo)args[2];

                ci.cancel();

                break;
            }
        }
    }
}
