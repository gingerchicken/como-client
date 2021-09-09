package net.como.client.cheats;

import net.como.client.structures.events.*;
import net.como.client.CheatClient;
import net.como.client.events.JumpEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.settings.Setting;

public class SuperJump extends Cheat {
    public SuperJump() {
        super("SuperJump");
        this.description = "Jump higher than you should.";

        this.addSetting(new Setting("UpwardSpeed", 2d));
    }

    @Override
    public void activate() {
        this.addListen(JumpEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(JumpEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "JumpEvent": {
                JumpEvent jumpEvent = (JumpEvent)event;

                jumpEvent.ci.cancel();

                // Get the upward speed.
                Double upwardSpeed = (Double)this.getSetting("UpwardSpeed").value;

                // Add the new speed.
                CheatClient.me().addVelocity(0, upwardSpeed, 0);
            }
        }
    }
}
