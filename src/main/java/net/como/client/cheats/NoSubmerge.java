package net.como.client.cheats;

import net.como.client.events.OnSubmersionTypeEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.minecraft.client.render.CameraSubmersionType;

public class NoSubmerge extends Cheat {
    public NoSubmerge() {
        super("NoSubmerge");
    }

    @Override
    public void activate() {
        this.addListen(OnSubmersionTypeEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnSubmersionTypeEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnSubmersionTypeEvent": {
                OnSubmersionTypeEvent e = (OnSubmersionTypeEvent)event;

                e.cir.setReturnValue(CameraSubmersionType.NONE);

                break;
            }
        }
    }
}
