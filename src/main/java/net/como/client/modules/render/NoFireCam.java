package net.como.client.modules.render;

import net.como.client.events.render.RenderFireOverlayEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public class NoFireCam extends Module {
    public NoFireCam() {
        super("NoFireCam");

        this.setDescription("Disables the annoying fire overlay to allow you to see a bit better.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(RenderFireOverlayEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderFireOverlayEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderFireOverlayEvent": {
                ((RenderFireOverlayEvent)event).ci.cancel();
                break;
            }
        }
    }
}
