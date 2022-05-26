package net.como.client.modules.packet;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import io.netty.buffer.Unpooled;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.GetClientModNameEvent;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.mixin.packet.CustomPayloadC2SPacketAccessor;
import net.como.client.modules.Module;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class FakeClient extends Module {
    public FakeClient() {
        super("FakeClient", true);

        this.setDescription("Makes the client appear as vanilla to any servers.");
        this.setCategory("Packet");

        this.addSetting(new Setting("Client", "vanilla"));
    }

    @Override
    public String listOption() {
        return this.getStringSetting("Client");
    }

    @Override
    public void activate() {
        this.addListen(SendPacketEvent.class);
        this.addListen(GetClientModNameEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);
        this.removeListen(GetClientModNameEvent.class);
    }

    private String getClientName() {
        return this.getStringSetting("Client");
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetClientModNameEvent": {
                GetClientModNameEvent e = (GetClientModNameEvent)event;

                e.cir.setReturnValue(this.getClientName());

                break;
            }

            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;

                // Shout out to the meteor developers for this one!

                if (!(e.packet instanceof CustomPayloadC2SPacket)) break;
                CustomPayloadC2SPacketAccessor packet = (CustomPayloadC2SPacketAccessor)(e.packet);
                
                Identifier id = packet.getChannel();

                // Set our client brand
                if (id.equals(CustomPayloadC2SPacket.BRAND)) {
                    packet.setData(
                        new PacketByteBuf(Unpooled.buffer()).writeString(
                            this.getClientName()
                        )
                    );

                    break;
                }

                // Cancel all packets to do with fabric
                if (StringUtils.containsIgnoreCase(packet.getData().toString(StandardCharsets.UTF_8), "fabric")) {
                    e.ci.cancel();

                    break;
                }

                break;
            }
        }
    }
}
