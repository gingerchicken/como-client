package net.como.client.modules.packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.como.client.ComoClient;
import net.como.client.events.client.BeginRenderTickEvent;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;
import net.como.client.misc.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketLimiter extends Module {

    int totalPackets = 0;
    int lastTotalPackets = 0;

    Queue<Packet<?>> packets = new ConcurrentLinkedDeque<>();

    public Integer totalDelayedPackets() {
        return this.packets.size();
    }

    @Override
    public String listOption() {
        Integer totalPackets = lastTotalPackets;

        return totalPackets.toString();
    }

    private void sendPackets() {
        long delay = (long)(this.getDoubleSetting("SendDelay") * 1000d);

        // Copy into another stack
        this.lastTotalPackets = this.totalDelayedPackets();
        Queue<Packet<?>> sendPackets = new ConcurrentLinkedDeque<>();
        while (this.totalDelayedPackets() > 0) {
            sendPackets.add(this.packets.remove());
        }

        totalPackets = 0;

        // Send them.
        Thread thread = new Thread(() -> {
            while (sendPackets.size() > 0 && ClientUtils.inGame()) {
                try {
                    Packet<?> packet = sendPackets.remove();
                    ComoClient.me().networkHandler.sendPacket(packet);
    
                    Thread.sleep(delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public PacketLimiter() {
        super("PacketLimiter");
        this.setDescription("Delays packets to prevent getting kicked for 'too many packets'.");

        this.setCategory("Packet");

        this.addSetting(new Setting("SendDelay", 0.05));
        this.addSetting(new Setting("MaxPackets", 10));
        this.addSetting(new Setting("IgnoreMovement", false));
    }
    
    @Override
    public void activate() {
        this.reset();

        this.addListen(ClientTickEvent.class);
        this.addListen(SendPacketEvent.class);
        this.addListen(BeginRenderTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(SendPacketEvent.class);
        this.removeListen(BeginRenderTickEvent.class);

        this.sendPackets();
    }

    private Boolean shouldSendPacket() {
        return this.totalPackets <= this.getIntSetting("MaxPackets");
    }

    private void reset() {
        this.totalPackets = 0;
        this.packets.clear();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                this.sendPackets();

                break;
            }

            case "BeginRenderTickEvent": {
                if (!ClientUtils.inGame() && (this.packets.size() > 0 || this.totalPackets > 0)) {
                    this.reset();
                }
                
                break;
            }

            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;

                // We don't want to delay keep alive packets.
                if (e.packet instanceof KeepAliveC2SPacket) break;

                // See if we still want to move.
                if (this.getBoolSetting("IgnoreMovement") && (e.packet instanceof PlayerMoveC2SPacket)) break;

                if (this.shouldSendPacket()) {
                    totalPackets++;

                    break;
                }

                this.packets.add(e.packet);
                e.ci.cancel();

                break;
            }
        }
    }
}
