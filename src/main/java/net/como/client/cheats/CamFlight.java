package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.minecraft.util.math.Vec3d;

public class CamFlight extends Cheat {
    public CamFlight() {
        super("Camera Flight");

        settings.addSetting(new Setting("Speed", 5d));
    }

    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onMovementPacket": {
                // This makes things a lot more controlable.
                if (!CheatClient.me().input.pressingForward) break;

                // Get the speed from our settings system.
                Double speed = (Double) this.settings.getSetting("Speed").value;

                // Calculate the velocity.
                Vec3d fasterVelocity = CheatClient.me().getRotationVector().multiply(new Vec3d(speed, speed, speed));

                // Update our velocity.
                CheatClient.me().setVelocity(fasterVelocity);
            }
        }
    }
}
