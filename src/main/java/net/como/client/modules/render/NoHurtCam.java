package net.como.client.modules.render;

import net.como.client.events.render.BobViewWhenHurtEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam");

        this.setDescription("Disables the screen rotation when getting damaged.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(BobViewWhenHurtEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(BobViewWhenHurtEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "BobViewWhenHurtEvent": {
                ((BobViewWhenHurtEvent)event).ci.cancel();
                break;
            }
        }
    }
}
