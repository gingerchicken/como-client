package net.como.client.modules.render;

import java.util.ArrayList;

import net.como.client.events.Event;
import net.como.client.events.client.GetEntitiesEvent;
import net.como.client.modules.Module;
import net.minecraft.entity.Entity;

public class NoEntityRender extends Module {

    public NoEntityRender() {
        super("NoEntityRender");
        
        this.setDescription("Blocks all entities from rendering.");

        this.setCategory("Render");
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetEntitiesEvent": {
                GetEntitiesEvent e = (GetEntitiesEvent) event;
                
                e.cir.setReturnValue(new ArrayList<Entity>());

                break;
            }
        }
    }

    @Override
    public void activate() {
        this.addListen(GetEntitiesEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetEntitiesEvent.class);
    }
    
}
