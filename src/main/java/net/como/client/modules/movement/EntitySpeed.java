package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.minecraft.entity.Entity;

public class EntitySpeed extends Module {
    public EntitySpeed() {
        super("EntitySpeed");

        this.addSetting(new Setting("Speed", 5d));
        this.addSetting(new Setting("Flight", false));
        this.addSetting(new Setting("ForceAngles", true));

        this.description = "Allows you to set a mounted entity's speed, you can also control entities without saddles.";
    }

    @Override
    public String listOption() {
        return this.getBoolSetting("Flight") ? "Flight" : "Travel";
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }
    
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                Entity ent = ComoClient.me().getVehicle();
                if (ent == null) break;

                if (this.getBoolSetting("ForceAngles")) {
                    ent.setYaw(ComoClient.me().getYaw());
                    ent.setPitch(ComoClient.me().getPitch());
                }

                ClientUtils.entitySpeedControl(ent, this.getDoubleSetting("Speed"), this.getBoolSetting("Flight"));

                break;
            }
        }
    }
}
