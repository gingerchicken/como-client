package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.AddParticleEmitterEvent;
import net.como.client.events.AddParticleEvent;
import net.como.client.events.ClientTickEvent;
import net.como.client.interfaces.mixin.IParticleManager;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.minecraft.client.particle.ParticleManager;

public class NoParticles extends Module {

    public NoParticles() {
        super("NoParticles");

        this.description = "Blocks any particles from being rendered";

        this.setCategory("Render");
    }
    
    @Override
    public void activate() {
        this.addListen(AddParticleEmitterEvent.class);
        this.addListen(AddParticleEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(AddParticleEvent.class);
        this.removeListen(AddParticleEmitterEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ParticleManager pm = ComoClient.getClient().particleManager;
                IParticleManager iPm = (IParticleManager)(pm);

                iPm.clearAll();

                break;
            }
            case "AddParticleEvent": {
                AddParticleEvent e = (AddParticleEvent)event;
                e.ci.cancel();

                break;
            }

            case "AddParticleEmitterEvent": {
                AddParticleEmitterEvent e = (AddParticleEmitterEvent)(event);
                e.ci.cancel();

                break;
            }
        }
    }
}
