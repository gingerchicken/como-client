package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;
import net.minecraft.client.MinecraftClient;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint");
        this.setDescription("Makes you sprint whenever you move.");

        this.setCategory("Movement");
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
                client.options.sprintKey.setPressed(true);

                break;
            }
        }
    }
}
