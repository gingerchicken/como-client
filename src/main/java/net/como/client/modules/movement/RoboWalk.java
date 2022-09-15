package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.client.PlayerMoveEvent;
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
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {

            case "SendPacketEvent": {
                MinecraftClient instance = ComoClient.getClient();
                SendPacketEvent e = (SendPacketEvent) event;
                Packet<?> packet = e.packet;

                if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.Full) {
                    e.ci.cancel();

                    PlayerMoveC2SPacket castPacket = (PlayerMoveC2SPacket) packet;

                    // Round x and z to two decimal places.
                    // Yes, this is the best answer on StackOverflow.
                    // https://stackoverflow.com/questions/5710394/how-do-i-round-a-double-to-two-decimal-places-in-java
                    double smoothX = Math.round(castPacket.getX(0) * 100) / 100;
                    double smoothZ = Math.round(castPacket.getZ(0) * 100) / 100;

                    long dx = ((long) (smoothX * 1000)) % 10; //simulate the check that liveoverflow runs
                    long dz = ((long) (smoothZ * 1000)) % 10;

                    if (dx != 0 || dz != 0) {
                        return;
                    }

                    Packet<?> clone;

                    if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                        clone = new PlayerMoveC2SPacket.PositionAndOnGround(smoothX, castPacket.getY(0), smoothZ, castPacket.isOnGround());
                    } else {
                        clone = new PlayerMoveC2SPacket.Full(smoothX, castPacket.getY(0), smoothZ, castPacket.getYaw(0), castPacket.getPitch(0), castPacket.isOnGround());
                    }

                    instance.player.networkHandler.getConnection().send(clone);
                } else if (packet instanceof VehicleMoveC2SPacket) {
                    VehicleMoveC2SPacket castPacket = (VehicleMoveC2SPacket) packet;

                    e.ci.cancel();

                    double smoothX = Math.round(castPacket.getX() * 100) / 100;
                    double smoothZ = Math.round(castPacket.getZ() * 100) / 100;

                    long dx = ((long) (smoothX * 1000)) % 10; //simulate the check that liveoverflow runs
                    long dz = ((long) (smoothZ * 1000)) % 10;

                    if (dx != 0 || dz != 0) {
                        return;
                    }

                    Entity vehicle = instance.player.getVehicle();

                    vehicle.setPos(smoothX, castPacket.getY(), smoothZ);

                    VehicleMoveC2SPacket movePacket = new VehicleMoveC2SPacket(vehicle);

                    instance.player.networkHandler.getConnection().send(movePacket);
                }
            }
        }
    }
}