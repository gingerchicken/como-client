package net.como.client.modules;

import net.como.client.ComoClient;
import net.como.client.events.DeathEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn");

        this.description = "Automatically respawns the player.";
    }

    @Override
    public void activate() {
        this.addListen(DeathEvent.class);;
    }

    @Override
    public void deactivate() {
        this.removeListen(DeathEvent.class);
    }
    
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "DeathEvent": {
                ComoClient.me().requestRespawn();
                break;
            }
        }
    }
}
