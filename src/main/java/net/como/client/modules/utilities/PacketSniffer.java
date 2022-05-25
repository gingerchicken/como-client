package net.como.client.modules.utilities;

import java.util.HashMap;

import net.como.client.ComoClient;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;
import net.como.client.misc.settings.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

public class PacketSniffer extends Module {
    private Integer capturedPackets = 0;
    private Double startTime = ComoClient.getCurrentTime();

    public PacketSniffer() {
        super("PacketSniffer");
        
        // TODO add inbound packets
        this.setDescription("Displays captured outbound packets in chat.");

        this.addSetting(new Setting("HidePacket", new HashMap<String, Boolean>()));

        this.setCategory("Utilities");
    }
    
    @Override
    public String listOption() {
        return this.capturedPackets.toString();
    }

    @Override
    public void activate() {
        this.capturedPackets = 0;
        startTime = ComoClient.getCurrentTime();
    
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);
    }

    private void displayPacket(Packet<?> packet) {
        this.displayMessage(String.format("Captured (%f) %s", ComoClient.getCurrentTime() - this.startTime, packet.getClass().getSimpleName()));
        this.capturedPackets++;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;

                Packet<?> packet = e.packet;
                if (this.getHashMapSetting("HidePacket").containsKey(packet.getClass().getSimpleName())) break;

                this.displayPacket(packet);

                if (packet instanceof CreativeInventoryActionC2SPacket) {
                    break;
                }

                break;
            }
        }
    }
}
