package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;

public class SuperJump extends Cheat {
    public SuperJump() {
        super("Super Jump");

        this.settings.addSetting(new Setting<Double>("UpwardSpeed", 2d));
    }

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        if (!this.isEnabled()) return;

        switch(eventName) {
            case "onJump": {
                CallbackInfo ci = (CallbackInfo)args[0];
                ci.cancel();

                // Get the upward speed.
                Double upwardSpeed = (Double)this.settings.getSetting("UpwardSpeed").value;

                // Add the new speed.
                CheatClient.me().addVelocity(0, upwardSpeed, 0);
            }
        }
    }
}
