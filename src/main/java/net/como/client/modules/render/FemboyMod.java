package net.como.client.modules.render;

import java.util.Random;
import java.util.UUID;

import net.como.client.ComoClient;
import net.como.client.components.FemboySkinHelper;
import net.como.client.events.GetSkinTextureEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public class FemboyMod extends Module {

    public FemboyMod() {
        super("FemboyMod");
    
        this.description = "Makes everyone's skin a femboy.";

        this.setCategory("Render");
    }
    
    @Override
    public String listOption() {
        long time = (long)ComoClient.getCurrentTime();
        return time % 5 == 0 ? "UwU" : "OwO";
    }

    @Override
    public void activate() {
        this.addListen(GetSkinTextureEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetSkinTextureEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetSkinTextureEvent": {
                GetSkinTextureEvent e = (GetSkinTextureEvent)(event);

                // Get the player's UUID
                UUID uuid = e.player.getUuid();

                // Set the skin
                e.cir.setReturnValue(FemboySkinHelper.getTexture(uuid, FemboySkinHelper.randomFromUuid(uuid)));

                break;
            }
        }
    }
}
