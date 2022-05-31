package net.como.client.modules.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.GetEntitiesEvent;
import net.como.client.events.render.RenderWorldEvent;
import net.como.client.modules.Module;
import net.como.client.utils.ClientUtils;
import net.minecraft.entity.Entity;

public class NoEntityRender extends Module {

    public NoEntityRender() {
        super("NoEntityRender");
        
        this.setDescription("Blocks all entities from rendering.");

        this.addSetting(new Setting("Whitelist", new HashMap<String, Boolean>()));

        this.setCategory("Render");
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldEvent": {
                Iterable<Entity> entities = ComoClient.getClient().world.getEntities();

                List<Entity> toRemove = new ArrayList<>();

                for (Entity entity : entities) {
                    String name = ClientUtils.getEntityType(entity);

                    // Don't disregard the player
                    if (entity == ComoClient.me()) continue;

                    if (this.getHashMapSetting("Whitelist").containsKey(name)) continue;
                    
                    toRemove.add(entity);
                }

                for (Entity entity : toRemove) {
                    entity.discard();
                }

                break;
            }
        }
    }

    @Override
    public void activate() {
        this.addListen(RenderWorldEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWorldEvent.class);
    }
    
}
