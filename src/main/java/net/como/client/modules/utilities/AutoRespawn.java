package net.como.client.modules.utilities;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.screen.DeathEvent;
import net.como.client.modules.Module;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn");

        this.setDescription("Automatically respawns the player.");
        this.setCategory("Utility");
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
