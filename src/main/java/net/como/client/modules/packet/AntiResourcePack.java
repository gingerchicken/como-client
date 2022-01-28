package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.OnResourcePackSendEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.ChatUtils;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;

public class AntiResourcePack extends Module {
    public AntiResourcePack() {
        super("AntiResourcePack");

        this.setCategory("Packet");

        this.description = "Deny all resource packs but say to the server that they got downloaded.";
    }

    @Override
    public void activate() {
        this.addListen(OnResourcePackSendEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnResourcePackSendEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    Boolean shouldSendPackets = false;

    public void sendAccept() {
        // "Accept it"
        ComoClient.me().networkHandler.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.ACCEPTED));
        
        // "It has downloaded"
        ComoClient.me().networkHandler.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
    }

    public void doBlock() {
        this.shouldSendPackets = true;
    }

    ResourcePackSendS2CPacket lastPacket = null;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (!this.shouldSendPackets) return;

                // Accept it.
                this.sendAccept();
                
                // Make sure that it doesn't happen again
                this.shouldSendPackets = false;

                // Generate a message
                String message = lastPacket == null
                ? "Blocked server resource pack!" 
                : String.format("Blocked resource pack from %s (SHA1: %s%s%s)", lastPacket.getURL(), ChatUtils.GREEN, lastPacket.getSHA1(), ChatUtils.WHITE);

                // Send the message
                this.displayMessage(message);

                // Reset
                lastPacket = null;

                break;
            }

            case "OnResourcePackSendEvent": {
                OnResourcePackSendEvent e = (OnResourcePackSendEvent)event;

                // Don't process it further.
                e.ci.cancel();

                // Set the packet
                this.lastPacket = e.packet;

                // Do the block next tick
                this.doBlock();
                
                break;
            }
        }
    }
}
