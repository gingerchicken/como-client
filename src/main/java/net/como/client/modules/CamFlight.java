package net.como.client.modules;

import net.como.client.CheatClient;
import net.como.client.events.PreMovementPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.util.math.Vec3d;

public class CamFlight extends Module {
    public CamFlight() {
        super("CameraFlight");

        this.addSetting(new Setting("Speed", 5d));

        this.description = "Fly quickly where ever your camera is looking.";
    }
  
    @Override
    public void activate() {
        this.addListen(PreMovementPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(PreMovementPacketEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "PreMovementPacketEvent": {
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
