package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.PlayerMoveEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.MathsUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Cheat {
    private Vec3d origin = new Vec3d(0, 0, 0);
    private float originPitch = 0, originYaw = 0;

    private void setOrigin() {
        this.origin = CheatClient.me().getPos();
        this.originYaw = CheatClient.me().getYaw();
        this.originPitch = CheatClient.me().getPitch();
        
    }

    private void revertOrigin() {
        CheatClient.me().setPos(origin.x, origin.y, origin.z);
        CheatClient.me().setYaw(this.originYaw);
        CheatClient.me().setPitch(this.originPitch);

        CheatClient.me().noClip = false;
    }

    public FreeCam() {
        super("FreeCam");

        this.addSetting(new Setting("Speed", 1f));

        this.description = "Allows you to fly around the world (but client-side)";
    }
    
    @Override
    public void activate() {
        this.setOrigin();

        this.addListen(ClientTickEvent.class);
        this.addListen(SendPacketEvent.class);
        this.addListen(PlayerMoveEvent.class);

        this.revertOrigin();
    }

    @Override
    public void deactivate() {
        this.revertOrigin();

        this.removeListen(ClientTickEvent.class);
        this.removeListen(SendPacketEvent.class);
        this.removeListen(PlayerMoveEvent.class);

        this.revertOrigin();
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
                if (e.packet instanceof PlayerMoveC2SPacket || e.packet instanceof ClientCommandC2SPacket) e.ci.cancel();

                break;
            }
            case "ClientTickEvent": {
                // Vals
                Vec3d v = Vec3d.ZERO;
                Vec3d pos = me.getPos();
                float speed = (float)this.getSetting("Speed").value;

                if (me.isSprinting()) speed *= 2;

                // Client settings
                me.setOnGround(true);
                me.setVelocity(0, 0, 0);

                // Game options
                GameOptions opt = CheatClient.getClient().options;

                // Controls
                if (opt.keyForward.isPressed()) v = v.add(CheatClient.me().getRotationVector());
                if (opt.keyBack.isPressed())    v = v.add(CheatClient.me().getRotationVector().multiply(-1));

                if (opt.keyRight.isPressed())   v = v.add(MathsUtils.getRightVelocity(CheatClient.me()));
                if (opt.keyLeft.isPressed())    v = v.add(MathsUtils.getRightVelocity(CheatClient.me()).multiply(-1));

                if (opt.keyJump.isPressed())    v = v.add(0,  1, 0);
                if (opt.keySneak.isPressed())   v = v.add(0, -1, 0);

                // Calculate the speed.
                v   = v.multiply(speed);
                pos = pos.add(v);

                // Set the velocity
                me.setPos(pos.x, pos.y, pos.z);

                break;
            }
        }
    }
}
