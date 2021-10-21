package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.PlayerMoveEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Cheat {
    private Vec3d origin = new Vec3d(0, 0, 0);

    public FreeCam() {
        super("FreeCam");

        this.addSetting(new Setting("Speed", 1f));
    }
    
    @Override
    public void activate() {
        origin = CheatClient.me().getPos();

        this.addListen(ClientTickEvent.class);
        this.addListen(SendPacketEvent.class);
        this.addListen(PlayerMoveEvent.class);
    }

    @Override
    public void deactivate() {
        CheatClient.me().setPos(origin.x, origin.y, origin.z);

        this.removeListen(ClientTickEvent.class);
        this.removeListen(SendPacketEvent.class);
        this.removeListen(PlayerMoveEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        ClientPlayerEntity me = CheatClient.me();

        switch (event.getClass().getSimpleName()) {
            case "PlayerMoveEvent": {
                me.noClip = true;
                break;
            }
            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;
                if (e.packet instanceof PlayerMoveC2SPacket) e.ci.cancel();

                break;
            }
            case "ClientTickEvent": {
                // Vals
                Vec3d v = Vec3d.ZERO;
                float speed = (float)this.getSetting("Speed").value;

                if (me.isSprinting()) speed *= 2;

                // Client settings
                me.setOnGround(false);
                me.noClip = true;
                me.flyingSpeed = speed;

                // Game options
                GameOptions opt = CheatClient.getClient().options;

                // Controls
                if (opt.keyJump.isPressed())    v = v.add(0,  1, 0);
                if (opt.keySneak.isPressed())   v = v.add(0, -1, 0);

                // Calculate the speed.
                v = v.multiply(speed);

                // Set the velocity
                me.setVelocity(v);
                break;
            }
        }
    }
}
