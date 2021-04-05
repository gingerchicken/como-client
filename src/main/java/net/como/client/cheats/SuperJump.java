package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;

public class SuperJump extends Cheat {
    public SuperJump() {
        super("Super Jump");
    }

    private double upwardVelocity = 2;

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        if (!this.isEnabled()) return;

        switch(eventName) {
            case "onJump": {
                CallbackInfo ci = (CallbackInfo)args[0];

                ci.cancel();
                CheatClient.me().addVelocity(0, this.upwardVelocity*2, 0);
            }
        }
    }
}
