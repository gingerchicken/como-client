package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.packet.PreMovementPacketEvent;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;
import net.como.client.misc.settings.Setting;
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

        this.setDescription("Fly with the elytra but without needing fireworks etc.");

        this.setCategory("Movement");
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
                    ClientUtils.entitySpeedControl(ComoClient.me(), this.getDoubleSetting("MaxSpeed"), true);

                break;
            }
        }
    }
}
