package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.Entity;

public class EntitySpin extends Module {
    public EntitySpin() {
        super("EntitySpin");

        this.setDescription("Rotates the ridden entity at varying speed.");

        this.addSetting(new Setting("Speed", 5d));
        
        this.addSetting(new Setting("Yaw", true));
        this.addSetting(new Setting("Pitch", true));

        this.setCategory("Movement");
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

                // Set our yaw and pitch
                if (this.getBoolSetting("Yaw"))     ent.setYaw(ent.getYaw()     + (float)(double)this.getDoubleSetting("Speed"));
                if (this.getBoolSetting("Pitch"))   ent.setPitch(ent.getPitch() + (float)(double)this.getDoubleSetting("Speed"));
                
                break;
            }
        }
    }
}
