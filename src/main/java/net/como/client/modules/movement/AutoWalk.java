package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk");
        this.setDescription("A simple module that just walks forward without you having to press anything.");

        // TODO make it stop when there is a hole ahead and add a setting for it.

        this.setCategory("Movement");
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    KeyBinding getForwardKey() {
        MinecraftClient client = ComoClient.getClient();

        return client.options.forwardKey;
    }

    private void go() {
        getForwardKey().setPressed(true);
    }

    private void stop() {
        getForwardKey().setPressed(false);
    }


    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.stop();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                this.go();
                break;
            }
        }
    }
}
