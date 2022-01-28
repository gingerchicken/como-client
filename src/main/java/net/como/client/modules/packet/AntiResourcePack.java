package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.events.OnResourcePackSendEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;

public class AntiResourcePack extends Module {
    public AntiResourcePack() {
        super("AntiResourcePack");

        this.setCategory("Packet");

        this.description = "Deny all resource packs but say to the server that they got downloaded.";
    }

    @Override
    public void activate() {
        this.addListen(OnResourcePackSendEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnResourcePackSendEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnResourcePackSendEvent": {
                OnResourcePackSendEvent e = (OnResourcePackSendEvent)event;

                // Don't process it further.
                e.ci.cancel();

                // "Accept it"
                ComoClient.me().networkHandler.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.ACCEPTED));
                
                // "It has downloaded"
                ComoClient.me().networkHandler.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
                
                break;
            }
        }
    }
}
