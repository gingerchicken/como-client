package net.como.client.modules;

import net.como.client.ComoClient;
import net.como.client.events.PreMovementPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.MathsUtils;
import net.minecraft.util.math.Vec3d;

public class ElytraFlight extends Module {
    public ElytraFlight() {
        super("ElytraFlight");

        this.addSetting(new Setting("MaxSpeed", 10d));
        // In theory, this is not the acceleration but rather a ratio between the old velocity and the new velocity - but I guess it is quicker to call it acceleration :P
        this.addSetting(new Setting("Acceleration", 1.1d));
        this.addSetting(new Setting("LegitMode", false));

        this.description = "Fly with the elytra but without needing fireworks etc.";
    }

    private void moveLegitMode() {
        if (!(ComoClient.me().input.pressingForward || ComoClient.me().input.pressingBack || ComoClient.me().input.pressingLeft || ComoClient.me().input.pressingRight)) return;

        // Get the required variables
        Double acceleration = (Double)this.getSetting("Acceleration").value;
        Double maxSpeed     = (Double)this.getSetting("MaxSpeed").value;

        Vec3d velocity      = ComoClient.me().getVelocity();

        // Multiply the acceleration by the current velocity
        velocity = velocity.multiply(acceleration);

        // Make sure that we are not going too quick
        if (velocity.distanceTo(new Vec3d(0, 0, 0)) > maxSpeed) return;

        ComoClient.me().setVelocity(velocity);
    }
    private void moveNormalMode() {
        // Initialize as still.
        Vec3d velocity = new Vec3d(0, 0, 0);

        // Get the required setting(s)
        Double maxSpeed = (Double)this.getSetting("MaxSpeed").value;

        // We only need these two velocities since the other you can calculate just by multiplying these out by -1 :P
		Vec3d forward = MathsUtils.getForwardVelocity(ComoClient.me());
        Vec3d right   = MathsUtils.getRightVelocity(ComoClient.me());

        // Forward + Back
        if (ComoClient.me().input.pressingForward) velocity = velocity.add(forward.multiply(new Vec3d(maxSpeed, 0, maxSpeed)));
        if (ComoClient.me().input.pressingBack)    velocity = velocity.add(forward.multiply(new Vec3d(-maxSpeed, 0, -maxSpeed)));

        // Right + Left
        if (ComoClient.me().input.pressingRight) velocity = velocity.add(right.multiply(new Vec3d(maxSpeed, 0, maxSpeed)));
        if (ComoClient.me().input.pressingLeft)  velocity = velocity.add(right.multiply(new Vec3d(-maxSpeed, 0, -maxSpeed)));

        // Up + Down
        if (ComoClient.me().input.jumping)  velocity = velocity.add(0, maxSpeed, 0);
        if (ComoClient.me().input.sneaking) velocity = velocity.add(0, -maxSpeed, 0);

        // Set the velocity
        ComoClient.me().setVelocity(velocity);
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
                // Make sure that we have an elytra equip
                if (!ClientUtils.hasElytraEquipt()) break;

                // Make sure that we are using the elytra
                if (!ComoClient.me().isFallFlying()) break;

                // Apply our new velocity
                if ((Boolean)this.getSetting("LegitMode").value)
                    this.moveLegitMode();
                else
                    this.moveNormalMode();

                break;
            }
        }
    }
}
