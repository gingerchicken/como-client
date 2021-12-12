package net.como.client;

// Oh no
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        ComoClient.log("Remember, it's only blockgame :V");
    }
}
