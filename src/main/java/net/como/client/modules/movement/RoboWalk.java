package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class RoboWalk extends Module {
    public RoboWalk() {
        super("RoboWalk");
        this.setDescription("Ensures that you always walk with exact co-ordinates (required for LiveOverflow server)");

        this.setCategory("Movement");
    }

    @Override
    public void activate() {
        this.ignoreNext = false;
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);
    }

    private double smooth(double value) {
        // Yes, this is the best answer on StackOverflow.
        // https://stackoverflow.com/questions/5710394/how-do-i-round-a-double-to-two-decimal-places-in-java
        // TODO - make this more efficient


        // Round to 2 decimal places
        return Math.round(value * 100.0d) / 100.0d;
    }

    private Vec3d smoothPacketPos(Vec3d position) {
        return new Vec3d(
            this.smooth(position.getX()),
            position.getY(),
            this.smooth(position.getZ())
        );
    }

    private Vec3d smoothPacketPos(PlayerMoveC2SPacket packet) {
        Vec3d curPos = Vec3d.ZERO; 
        // ... Might be ComoClient.me().getPos();
        
        return this.smoothPacketPos(
            new Vec3d(
                packet.getX(curPos.getX()),
                packet.getY(curPos.getY()),
                packet.getZ(curPos.getZ())
            )
        );
    }

    private Vec3d smoothPacketPos(VehicleMoveC2SPacket packet) {
        return this.smoothPacketPos(
            new Vec3d(
                packet.getX(),
                packet.getY(),
                packet.getZ()
            )
        );
    }
    
    private Vec3d smoothPacketPos(double x, double y, double z) {
        return this.smoothPacketPos(new Vec3d(x, y, z));
    }

    /**
     * Simulates live overflow's truncation
     * @param value The value to truncate
     * @return The truncated value
     */
    private long loTruncate(double value) {
        return ((long) (value * 1000d)) % 10;
    }

    private boolean ignoreNext = false;

    private boolean doIgnore() {
        if (!this.ignoreNext) return false;

        this.ignoreNext = false;

        return true;
    }

    /**
     * Sends the packet if ignore flag is disabled
     * @param packet
     */
    private void sendPacket(Packet<?> packet) {
        // It is important to ignore the case else we would be sending the packet until the closest to unacceptable case is met
        // So it would approach zero but never true meet it
        // I hope that makes sense?
        
        // Handle the ignore
        if (this.doIgnore()) return;

        // Send the packet
        ComoClient.me().networkHandler.getConnection().send(packet);

        // Don't capture the next packet
        this.ignoreNext = true;
    }

    /**
     * Simulates LiveOverflow's checks
     * @param smoothPos The smoothed position
     * @return Whether the position should be ignored
     */
    private boolean shouldSend(Vec3d smoothPos) {
        // simulate the check that liveoverflow runs
        long dx = this.loTruncate(smoothPos.getX());
        long dz = this.loTruncate(smoothPos.getZ());

        return !(dx != 0 || dz != 0);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {

            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent) event;
                Packet<?> packet = e.packet;

                // Handle natural movement
                if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.Full) {
                    e.ci.cancel();

                    // Get the movement packet
                    PlayerMoveC2SPacket castPacket = (PlayerMoveC2SPacket) packet;

                    // Get the smooth position
                    Vec3d smoothPos = this.smoothPacketPos(castPacket);

                    // simulate the check that liveoverflow runs
                    if (!this.shouldSend(smoothPos)) return;

                    // Create the new packet
                    Packet<?> clone = packet instanceof PlayerMoveC2SPacket.PositionAndOnGround
                        ? new PlayerMoveC2SPacket.PositionAndOnGround(smoothPos.getX(), smoothPos.getY(), smoothPos.getZ(), castPacket.isOnGround())
                        : new PlayerMoveC2SPacket.Full(smoothPos.getX(), smoothPos.getY(), smoothPos.getZ(), castPacket.getYaw(0), castPacket.getPitch(0), castPacket.isOnGround());

                    // Send the packet
                    this.sendPacket(clone);
                    break;
                }

                // Handle vehicle movement
                if (packet instanceof VehicleMoveC2SPacket) {
                    e.ci.cancel();

                    // Get the movement packet
                    VehicleMoveC2SPacket castPacket = (VehicleMoveC2SPacket) packet;

                    // Get the smooth position
                    Vec3d smoothPos = this.smoothPacketPos(castPacket);

                    // simulate the check that liveoverflow runs
                    if (!this.shouldSend(smoothPos)) return;

                    Entity vehicle = ComoClient.me().getVehicle();
                    vehicle.setPos(smoothPos.getX(), smoothPos.getY(), smoothPos.getZ());

                    // Create the new packet
                    VehicleMoveC2SPacket movePacket = new VehicleMoveC2SPacket(vehicle);

                    this.sendPacket(movePacket);
                    break;
                }
            
                // Ignore other cases
                break;
            }
        }
    }
}