package net.como.client.modules.render;

import java.util.Random;
import java.util.UUID;

import net.como.client.ComoClient;
import net.como.client.components.FemboySkinHelper;
import net.como.client.events.GetModelEvent;
import net.como.client.events.GetSkinTextureEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.util.Identifier;

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
        this.addListen(GetModelEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetModelEvent.class);
        this.removeListen(GetSkinTextureEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetSkinTextureEvent": {
                GetSkinTextureEvent e = (GetSkinTextureEvent)(event);

                // Get the player's UUID
                UUID uuid = e.player.getUuid();

                // Get a random
                Random random = FemboySkinHelper.randomFromUuid(uuid);

                // Get the skin id
                Identifier id = FemboySkinHelper.getTexture(uuid, random, FemboySkinHelper.randomModel(random).equals("slim"));

                // Set the skin
                e.cir.setReturnValue(id);

                break;
            }

            case "GetModelEvent": {
                GetModelEvent e = (GetModelEvent)event;

                // Get the player's UUID
                UUID uuid = e.player.getUuid();

                // Get a random
                Random random = FemboySkinHelper.randomFromUuid(uuid);

                e.cir.setReturnValue(FemboySkinHelper.randomModel(random));

                break;
            }
        }
    }
}
