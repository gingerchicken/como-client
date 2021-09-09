package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.DeathEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class AutoRespawn extends Cheat {
    public AutoRespawn() {
        super("AutoRespawn");
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
                CheatClient.me().requestRespawn();
                break;
            }
        }
    }
}
