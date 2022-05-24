package net.como.client.modules.packet;

import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry");

        this.setDescription("Allows odd behavior such as storing items in the crafting table slot.");
        this.setCategory("Packet");
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
                SendPacketEvent e = (SendPacketEvent)event;

                if (e.packet instanceof CloseHandledScreenC2SPacket) {
                    CloseHandledScreenC2SPacket packet = (CloseHandledScreenC2SPacket)e.packet;

                    // TODO add a mixin and properly check that it is the survival inv screen.
                    if (packet.getSyncId() == 0) e.ci.cancel();
                }
                
                break;
            }
        }
    }
}
