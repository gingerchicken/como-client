package net.como.client.modules;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.client.MinecraftClient;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint");
        this.description = "Makes you sprint whenever you move.";
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                MinecraftClient client = ComoClient.getClient();
                client.options.keySprint.setPressed(true);

                break;
            }
        }
    }
}