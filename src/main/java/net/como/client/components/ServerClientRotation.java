package net.como.client.components;

import net.como.client.events.PostMovementPacketEvent;
import net.como.client.events.PreMovementPacketEvent;
import net.como.client.interfaces.ModulePlugin;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.RotationUtils;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.util.math.Vec3d;

public class ServerClientRotation implements ModulePlugin {
    public void handlePreMotion() {
        this.client = ClientUtils.getRotation();
        ClientUtils.applyRotation(this.server);
    }
    public void handlePostMotion() {
        ClientUtils.applyRotation(this.client);

        active = false;
    }

    @Override
    public void addListeners(Module parentModule) {
        parentModule.addListen(PreMovementPacketEvent.class);
        parentModule.addListen(PostMovementPacketEvent.class);
    }

    @Override
    public void removeListeners(Module parentModule) {
        parentModule.removeListen(PreMovementPacketEvent.class);
        parentModule.removeListen(PostMovementPacketEvent.class);
    }

    private Rotation server;
    private Rotation client;

    private boolean active = false;


    public void lookAtPosServer(Vec3d pos) {
        this.active = true;

        this.server = RotationUtils.getRequiredRotation(pos);
    }
    public void lookAtPosClient(Vec3d pos) {
        ClientUtils.lookAtPos(pos);
    }

    public Rotation getServerRotation() {
        return this.active ? this.server : ClientUtils.getRotation();
    }

    public boolean fireEvent(Event event) {
        if (!active) return false;

        switch (event.getClass().getSimpleName()) {
            case "PostMotionEvent": {
                this.handlePostMotion();
                return true;
            }

            case "PreMotionEvent": {
                this.handlePreMotion();
                return true;
            }
        }

        return false;
    }
}
