package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.minecraft.client.util.math.Vector3d;

public class Flight extends Cheat {

    public Flight() {
        super("Fly Mode");

        this.description = "Basic flight (a bit terrible tbh).";
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