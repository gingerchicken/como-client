package net.como.client.modules.render;

import java.util.Random;
import java.util.UUID;

import net.como.client.ComoClient;
import net.como.client.components.FemboySkinHelper;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.GetModelEvent;
import net.como.client.events.GetSkinTextureEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.util.Identifier;

public class FemboyMod extends Module {

    public FemboyMod() {
        super("FemboyMod");
    
        this.setDescription("Makes everyone's skin a femboy.");

        this.setCategory("Render");
    }

    private String funnyFace = "OwO";
    
    @Override
    public String listOption() {
        return funnyFace;
    }

    @Override
    public void activate() {
        this.addListen(GetSkinTextureEvent.class);
        this.addListen(GetModelEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetModelEvent.class);
        this.removeListen(GetSkinTextureEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    private Long ticks = 0l;
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ticks = ticks % 200;
                funnyFace = (ticks < 2) ? "UwU" : "OwO";

                ticks++;
                break;
            }
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
