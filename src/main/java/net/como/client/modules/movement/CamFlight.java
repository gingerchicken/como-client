package net.como.client.modules.movement;

import net.como.client.ComoClient;
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
        
        this.setCategory("Movement");
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
                if (!ComoClient.me().input.pressingForward) break;

                // Get the speed from our settings system.
                Double speed = (Double) this.getSetting("Speed").value;

                // Calculate the velocity.
                Vec3d fasterVelocity = ComoClient.me().getRotationVector().multiply(new Vec3d(speed, speed, speed));

                // Update our velocity.
                ComoClient.me().setVelocity(fasterVelocity);
            }
        }
    }
}
