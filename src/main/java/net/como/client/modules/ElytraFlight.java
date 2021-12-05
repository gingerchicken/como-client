package net.como.client.modules;

import net.como.client.CheatClient;
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
        if (!(CheatClient.me().input.pressingForward || CheatClient.me().input.pressingBack || CheatClient.me().input.pressingLeft || CheatClient.me().input.pressingRight)) return;

        // Get the required variables
        Double acceleration = (Double)this.getSetting("Acceleration").value;
        Double maxSpeed     = (Double)this.getSetting("MaxSpeed").value;

        Vec3d velocity      = CheatClient.me().getVelocity();

        // Multiply the acceleration by the current velocity
        velocity = velocity.multiply(acceleration);

        // Make sure that we are not going too quick
        if (velocity.distanceTo(new Vec3d(0, 0, 0)) > maxSpeed) return;

        CheatClient.me().setVelocity(velocity);
    }
    private void moveNormalMode() {
        // Initialize as still.
        Vec3d velocity = new Vec3d(0, 0, 0);

        // Get the required setting(s)
        Double maxSpeed = (Double)this.getSetting("MaxSpeed").value;

        // We only need these two velocities since the other you can calculate just by multiplying these out by -1 :P
		Vec3d forward = MathsUtils.getForwardVelocity(CheatClient.me());
        Vec3d right   = MathsUtils.getRightVelocity(CheatClient.me());

        // Forward + Back
        if (CheatClient.me().input.pressingForward) velocity = velocity.add(forward.multiply(new Vec3d(maxSpeed, 0, maxSpeed)));
        if (CheatClient.me().input.pressingBack)    velocity = velocity.add(forward.multiply(new Vec3d(-maxSpeed, 0, -maxSpeed)));

        // Right + Left
        if (CheatClient.me().input.pressingRight) velocity = velocity.add(right.multiply(new Vec3d(maxSpeed, 0, maxSpeed)));
        if (CheatClient.me().input.pressingLeft)  velocity = velocity.add(right.multiply(new Vec3d(-maxSpeed, 0, -maxSpeed)));

        // Up + Down
        if (CheatClient.me().input.jumping)  velocity = velocity.add(0, maxSpeed, 0);
        if (CheatClient.me().input.sneaking) velocity = velocity.add(0, -maxSpeed, 0);

        // Set the velocity
        CheatClient.me().setVelocity(velocity);
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
                if (!CheatClient.me().isFallFlying()) break;

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
