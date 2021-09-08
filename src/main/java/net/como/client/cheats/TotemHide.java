package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.RenderItemEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.render.model.json.ModelTransformation;

public class TotemHide extends Cheat {
    // boolean smallTotem = false;

    public TotemHide() {
        super("TotemHide");

        this.addSetting(new Setting("SmallMode", false));

        this.description = "Change the display of the totem.";
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
                if (e.entity != CheatClient.me()) break;

                // Only left hand/first person
                if (e.renderMode != ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND) break;
                
                // Only do it for totems
                if (e.stack.getItem().toString() != "totem_of_undying") break;

                // If they just want to hide it, do so.
                if (!(boolean)this.getSetting("SmallMode").value) {
                    e.ci.cancel();
                } else {
                    // Else...Render the small totem
                    e.mStack.scale(0.5f, 0.5f, 0.5f);
                    e.mStack.translate(-0.5f, 0.0f, 0.0f);
                }

                break;
            }
        
        }
    }
}