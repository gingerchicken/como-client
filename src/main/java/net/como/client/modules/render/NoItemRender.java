package net.como.client.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.Module;

public class NoItemRender extends Module {
    public NoItemRender() {
        super("NoItemRender");

        this.setDescription("Hide all dropped items so then your friends cannot kill your client repeatedly.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                List<Entity> items = new ArrayList<>();

                // Get all the entities
                for (Entity entity : ComoClient.getClient().world.getEntities()) {
                    if (!(entity instanceof ItemEntity)) continue;

                    items.add(entity);
                }

                // Delete all
                for (Entity entity : items) entity.discard();

                break;
            }
        }
    }
}
