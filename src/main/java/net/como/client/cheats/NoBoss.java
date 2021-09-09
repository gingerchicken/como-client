package net.como.client.cheats;

import net.como.client.events.BossBarHudRenderEvent;
import net.como.client.events.BossBarHudSkyEffectsEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class NoBoss extends Cheat {
    public NoBoss() {
        super("NoBoss");

        this.description = "Hide annoying boss bars and their effects.";
    }
  
    @Override
    public void activate() {
        this.addListen(BossBarHudRenderEvent.class);
        this.addListen(BossBarHudSkyEffectsEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(BossBarHudRenderEvent.class);
        this.removeListen(BossBarHudSkyEffectsEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "BossBarHudRenderEvent": {
                ((BossBarHudRenderEvent)event).ci.cancel();
                break;
            }
            case "BossBarHudSkyEffectsEvent": {
                ((BossBarHudSkyEffectsEvent)event).cir.setReturnValue(false);;
                break;
            }
        }
    }
}
