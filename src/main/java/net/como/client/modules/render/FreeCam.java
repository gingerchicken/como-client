package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.PlayerMoveEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.events.UpdateCameraEvent;
import net.como.client.interfaces.mixin.ICamera;
import net.como.client.interfaces.mixin.IGameRenderer;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.MathsUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Module {
    public FreeCam() {
        super("FreeCam");

        this.addSetting(new Setting("Speed", 1f));

        this.description = "Allows you to fly around the world (but client-side)";

        this.setCategory("Render");
    }
    
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(UpdateCameraEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(UpdateCameraEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        ClientPlayerEntity me = ComoClient.me();

        switch (event.getClass().getSimpleName()) {
            case "UpdateCameraEvent": {
                UpdateCameraEvent e = (UpdateCameraEvent)event;
                e.ci.cancel();

                break;
            }
            case "ClientTickEvent": {
                // Vals
                Vec3d v = Vec3d.ZERO;

                // Game Renderer
                GameRenderer gameRenderer   = ComoClient.getClient().gameRenderer;
                IGameRenderer iGameRenderer =  (IGameRenderer)(gameRenderer);

                // Camera
                Camera cam = iGameRenderer.getCamera();
                ICamera iCam = (ICamera)cam;

                // Camera position
                Vec3d pos = cam.getPos();

                // Speed
                float speed = (float)this.getSetting("Speed").value;

                if (me.isSprinting()) speed *= 2;

                // Game options
                GameOptions opt = ComoClient.getClient().options;

                // Controls
                if (opt.forwardKey.isPressed()) v = v.add(ComoClient.me().getRotationVector());
                if (opt.backKey.isPressed())    v = v.add(ComoClient.me().getRotationVector().multiply(-1));

                if (opt.rightKey.isPressed())   v = v.add(MathsUtils.getRightVelocity(ComoClient.me()));
                if (opt.leftKey.isPressed())    v = v.add(MathsUtils.getRightVelocity(ComoClient.me()).multiply(-1));

                if (opt.jumpKey.isPressed())    v = v.add(0,  1, 0);
                if (opt.sneakKey.isPressed())   v = v.add(0, -1, 0);

                // Calculate the speed.
                v = v.multiply(speed);

                // Calculate next position
                pos = pos.add(v);

                // Set the camera position
                iCam.forceSetPos(pos);

                iCam.setPitch(ComoClient.me().getPitch());
                iCam.setYaw(ComoClient.me().getYaw());

                break;
            }
        }
    }
}
