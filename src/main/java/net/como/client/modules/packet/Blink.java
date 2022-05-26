package net.como.client.modules.packet;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.misc.settings.Setting;
import net.como.client.modules.Module;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;

public class Blink extends Module {

    public Blink() {
        super("Blink");

        this.setDescription("Delay your packets being sent.");

        this.addSetting(new Setting("SendDelay", 0.0d));
        this.addSetting(new Setting("Threaded", true));

        this.setCategory("Packet");
    }
  
    @Override
    public String listOption() {
        return ((Integer)(this.packets.size())).toString();
    }

    List<Packet<?>> packets = new ArrayList<>();

    /**
     * Used to send delayed packets back to the server
     */
    public void sendPackets(Boolean threaded) {
        ClientPlayNetworkHandler handler = ComoClient.me().networkHandler;
        while (packets.size() > 0 && !this.isEnabled()) {
            // Pop the first element of the array (i.e. the first in)
            Packet<?> packet = packets.remove(0);

            // Send it
            handler.sendPacket(packet);

            // Pause if multi-threaded
            try {
                if (threaded) Thread.sleep((long)(this.getDoubleSetting("SendDelay") * 1000));
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    @Override
    public void activate() {
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);

        // Handle threading
        if (this.getBoolSetting("Threaded")) {
            Thread thread = new Thread(() -> {
                this.sendPackets(true);
            });
    
            thread.start();
        } else {
            this.sendPackets(false);
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;
                Packet<?> packet = e.packet;

                // Still send keep alive packets
                if (packet instanceof KeepAliveC2SPacket) break;

                e.ci.cancel();
                this.packets.add(packet);

                break;
            }
        }
    }
}