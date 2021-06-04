package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;

public class Blink extends Cheat {

    public Blink() {
        super("Blink Mode");

        this.description = "Delay your packets being sent.";
    }

    // TODO this is not how blink works lol - store the packets and send them.
    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onMovementPacket": {
                CallbackInfo ci = (CallbackInfo)args[0];

                // Just don't send movement packets.
                ci.cancel();
            }
        }
    }
}