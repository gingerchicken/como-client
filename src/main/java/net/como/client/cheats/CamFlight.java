package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.MovementPacketEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.util.math.Vec3d;

public class CamFlight extends Cheat {
    public CamFlight() {
        super("CameraFlight");

        this.addSetting(new Setting("Speed", 5d));

        this.description = "Fly quickly where ever your camera is looking.";
    }
  
    @Override
    public void activate() {
        this.addListen(MovementPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(MovementPacketEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "MovementPacketEvent": {
                // This makes things a lot more controllable.
                if (!CheatClient.me().input.pressingForward) break;

                // Get the speed from our settings system.
                Double speed = (Double) this.getSetting("Speed").value;

                // Calculate the velocity.
                Vec3d fasterVelocity = CheatClient.me().getRotationVector().multiply(new Vec3d(speed, speed, speed));

                // Update our velocity.
                CheatClient.me().setVelocity(fasterVelocity);
            }
        }
    }
}
