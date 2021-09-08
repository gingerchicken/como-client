package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.settings.Setting;

public class SuperJump extends Cheat {
    public SuperJump() {
        super("SuperJump");

        this.addSetting(new Setting("UpwardSpeed", 2d));

        this.description = "Jump higher than you should.";
    }

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        if (!this.isEnabled()) return;

        switch(eventName) {
            case "onJump": {
                CallbackInfo ci = (CallbackInfo)args[0];
                ci.cancel();

                // Get the upward speed.
                Double upwardSpeed = (Double)this.getSetting("UpwardSpeed").value;

                // Add the new speed.
                CheatClient.me().addVelocity(0, upwardSpeed, 0);
            }
        }
    }
}
