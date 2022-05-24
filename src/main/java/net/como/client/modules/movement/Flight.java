package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.client.network.ClientPlayerEntity;

public class Flight extends Module {

    public Flight() {
        super("Flight");

        this.setDescription("Basic flight (a bit terrible tbh).");

        this.setCategory("Movement");
    }
    
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);

        ComoClient.me().getAbilities().allowFlying = false;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ClientPlayerEntity me = ComoClient.me();
                if (me == null) return;

                me.getAbilities().allowFlying = true;
            }
        }
    }
}