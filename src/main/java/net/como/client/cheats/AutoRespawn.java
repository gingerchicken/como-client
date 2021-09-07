package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;

public class AutoRespawn extends Cheat {
    public AutoRespawn() {
        super("AutoRespawn");
    }
    
    @Override
    public void receiveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onDeath": {
                // Request respawn
                CheatClient.me().requestRespawn();

                break;
            }
        }
    }
}
