package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;

public class Blink extends Cheat {

    public Blink() {
        super("Blink Mode");
    }

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onMovementPacket": {
                if (!this.isEnabled()) break;

                CallbackInfo ci = (CallbackInfo)args[0];
                ci.cancel();
            }
        }
    }
}