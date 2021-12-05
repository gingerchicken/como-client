package net.como.client.modules;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.PreMovementPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;

import net.minecraft.util.math.Vec3d;

public class SpeedHack extends Module {
    public SpeedHack() {
        super("Speed");

        this.addSetting(new Setting("Acceleration", 2.2d));
        this.addSetting(new Setting("MaxSpeed", 2d));

        this.description = "Go quicker than normal.";
    }

    private double getSpeed(Vec3d vec) {
        return vec.multiply(1, 0, 1).distanceTo(new Vec3d(0, 0, 0));
    }

    private Vec3d getNewVelocity() {
        // Get the current velocities and vectors.
        Vec3d rotVector = ComoClient.me().getRotationVector();
        Vec3d actualVelocity = ComoClient.me().getVelocity();

        // Initialise the faster velocity (i.e. the result)
        Vec3d fasterVelocity = new Vec3d(0, 0, 0);

        // Get the settings
        Double maxSpeed = (Double) this.getSetting("MaxSpeed").value;
        Double acceleration = (Double) this.getSetting("Acceleration").value;

        // Cannot get anything nice to work for the life of me so...
        if (ComoClient.me().input.pressingLeft || ComoClient.me().input.pressingRight) {
            
            // Accelerate the localplayer
            fasterVelocity = ComoClient.me().isOnGround() ? actualVelocity.multiply(acceleration, 1, acceleration) : actualVelocity;
        } else if (ComoClient.me().input.pressingForward || ComoClient.me().input.pressingBack) {

            // If we are trying to go backwards, just invert the velocity.
            if (ComoClient.me().input.pressingBack) rotVector = rotVector.multiply(-1, -1, -1);

            // Multiply out the rotation vector with the maximum speed but make the horizontal component zero.
            fasterVelocity = rotVector.multiply(maxSpeed, 0, maxSpeed);

            // Add back the horizontal component back to the velocity.
            fasterVelocity = fasterVelocity.add(0, actualVelocity.y, 0);
        }

        // TODO maybe optimise this by doing this check before the maths?
        // If we are going to fast, make sure that we ignore what we just did and return the current velocity since we cannot go quicker than that one.
        return this.getSpeed(fasterVelocity) > maxSpeed ? actualVelocity : fasterVelocity;
    }

    @Override
    public void activate() {
        this.addListen(PreMovementPacketEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(PreMovementPacketEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "PreMovementPacketEvent": {
                if (ComoClient.me().isOnGround() || ComoClient.me().isTouchingWater())
                    ComoClient.me().setVelocity(this.getNewVelocity());
                
                break;
            }
            case "ClientTickEvent": {
                // If we are not moving, don't move ;)
                if (!(ComoClient.me().input.pressingForward || ComoClient.me().input.pressingBack || ComoClient.me().input.pressingLeft || ComoClient.me().input.pressingRight)) {
                    // Still... but not flying...
                    Vec3d curr = ComoClient.me().getVelocity();
                    
                    ComoClient.me().setVelocity(curr.multiply(new Vec3d(0, 1, 0)));
                }

                break;
            }
        }
    }
}