package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;

public class Flight extends Cheat {

    public Flight() {
        super("Fly Mode");
    }

    public boolean defaultFlight = false;

    // TODO Add this on spawn?
    @Override
    public void activate() {
        CheatClient.me().abilities.allowFlying = true;
    }

    @Override
    public void deactivate() {
        CheatClient.me().abilities.allowFlying = false;
    }
}