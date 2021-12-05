package net.como.client.modules;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.minecraft.client.network.ClientPlayerEntity;

public class Flight extends Cheat {

    public Flight() {
        super("Flight");

        this.description = "Basic flight (a bit terrible tbh).";
    }
    
    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);

        CheatClient.me().getAbilities().allowFlying = false;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ClientPlayerEntity me = CheatClient.me();
                if (me == null) return;

                me.getAbilities().allowFlying = true;
            }
        }
    }
}