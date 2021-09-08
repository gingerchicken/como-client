package net.como.client.cheats;

import net.como.client.structures.events.Event;
import net.como.client.events.RenderEntityEvent;
import net.como.client.structures.Cheat;

public class AntiItemDrop extends Cheat {
    public AntiItemDrop() {
        super("NoItemRender");

        this.description = "Hide all dropped items so then your friends cannot kill your client repeatedly.";
    }

    @Override
    public void activate() {
        this.addListen(RenderEntityEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderEntityEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderEntityEvent": {
                RenderEntityEvent renderEntityEvent = (RenderEntityEvent)event;

                if (renderEntityEvent.entity instanceof net.minecraft.entity.ItemEntity) renderEntityEvent.ci.cancel();
                break;
            }
        }
    }
}
