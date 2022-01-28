package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.events.PreMovementPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.ClientUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall");

        this.description = "Take less fall damage.";

        this.setCategory("Packet");
    }

    @Override
    public void activate() {
        this.addListen(PreMovementPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(PreMovementPacketEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "PreMovementPacketEvent": {
                // Get the localplayer.
                ClientPlayerEntity player = ComoClient.me();

                // Use to make sure that flight is less gittery
                if(player.fallDistance <= (player.isFallFlying() ? 1 : 2)) break;

                if (player.isFallFlying()) {
                    // Make sure that the player is not falling too quickly
                    if(player.isSneaking() && player.getVelocity().getY() < -0.5) break;

                    // Make sure that the player is not elytra flying
                    if (ClientUtils.hasElytraEquipt()) break;
                }
                
                // I believe this just says to the server "ay yo, I am on floor dw 'bout it sweet cheeks :3"
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
                
                break;
            }
        }
    }
}
