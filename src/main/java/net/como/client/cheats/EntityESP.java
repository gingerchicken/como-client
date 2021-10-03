package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderWorldViewBobbingEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;


public class EntityESP extends Cheat {
    public EntityESP() {
        super("EntityESP");

        this.addSetting(new Setting("BoxPadding", 0f));
        this.addSetting(new Setting("BlendBoxes", false));

        this.addSetting(new Setting("BoundingBox", true));
        this.addSetting(new Setting("Tracers", true));

        this.description = "Know where entities are more easily.";
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(RenderWorldViewBobbingEvent.class);
    }

    @Override
	public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(RenderWorldViewBobbingEvent.class);
	}

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldViewBobbingEvent": {
                RenderWorldViewBobbingEvent e = (RenderWorldViewBobbingEvent)event;
                if ((Boolean)this.getSetting("Tracers").value) {
                    e.cancel = true;
                }

                break;
            }

            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;
                Iterable<Entity> ents = CheatClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    // No render myself.
                    if (entity instanceof PlayerEntity && (PlayerEntity)entity == CheatClient.me()) {
                        continue;
                    }

                    // Render tracers
                    if ((Boolean)this.getSetting("Tracers").value) {
                        RenderUtils.drawTracer(e.mStack, MathsUtils.getLerpedCentre(entity, e.tickDelta), e.tickDelta);
                    }

                    // Render mob box
                    if ((Boolean)this.getSetting("BoundingBox").value) {
                        RenderUtils.renderBox(entity, e.tickDelta, e.mStack, (Boolean)this.getSetting("BlendBoxes").value, (Float)this.getSetting("BoxPadding").value);
                    }
                }

                break;
            }
        }
    }
}
