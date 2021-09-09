package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;

public class Flight extends Cheat {

    public Flight() {
        super("FlyMode");

        this.description = "Basic flight (a bit terrible tbh).";
    }

    public boolean defaultFlight = false;

    // TODO Add this on spawn?
    @Override
    public void activate() {
        CheatClient.me().getAbilities().allowFlying = true;
    }

    @Override
    public void deactivate() {
        CheatClient.me().getAbilities().allowFlying = false;
    }
}