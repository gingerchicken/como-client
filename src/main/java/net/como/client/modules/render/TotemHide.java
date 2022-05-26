package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.render.RenderItemEvent;
import net.como.client.misc.Module;
import net.minecraft.client.render.model.json.ModelTransformation;

public class TotemHide extends Module {
    public TotemHide() {
        super("TotemHide");

        this.setDescription("Hide the totem item.");
        this.setCategory("Render");
    }
    
    @Override
    public void activate() {
        this.addListen(RenderItemEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderItemEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderItemEvent": {
                // Get the event
                RenderItemEvent e = (RenderItemEvent)event;

                // We want to see it if there are other players.
                if (e.entity != ComoClient.me()) break;

                // Only left hand/first person
                if (e.renderMode != ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND) break;
                
                // Only do it for totems
                if (e.stack.getItem().toString() != "totem_of_undying") break;

                // Don't render the totem
                e.ci.cancel();
               
                break;
            }
        
        }
    }
}