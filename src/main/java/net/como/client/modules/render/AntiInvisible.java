package net.como.client.modules.render;

import net.como.client.events.IsEntityInvisibleEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public class AntiInvisible extends Module {
    public AntiInvisible() {
        super("AntiInvisible");

        this.setDescription("Makes all invisible entities visible.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(IsEntityInvisibleEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(IsEntityInvisibleEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "IsEntityInvisibleEvent": {
                IsEntityInvisibleEvent e = (IsEntityInvisibleEvent)event;

                e.cir.setReturnValue(false);

                break;
            }
        }
    }
}
