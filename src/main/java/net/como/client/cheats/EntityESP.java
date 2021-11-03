package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.IsEntityGlowingEvent;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderWorldViewBobbingEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;


public class EntityESP extends Cheat {
    private final static int MODE_GLOW      = 1;
    private final static int MODE_MOB_BOX   = 2;

    public EntityESP() {
        super("EntityESP");

        this.addSetting(new Setting("BoxPadding", 0f));
        this.addSetting(new Setting("BlendBoxes", false));

        this.addSetting(new Setting("DrawMode", MODE_GLOW));

        this.description = "Know where entities are more easily.";
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(IsEntityGlowingEvent.class);
    }

    @Override
	public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(IsEntityGlowingEvent.class);
	}

    private boolean shouldRender(Entity entity) {
        return !(entity instanceof PlayerEntity && (PlayerEntity)entity == CheatClient.me());
    }

    private int getDrawMode() {
        return (int)this.getSetting("DrawMode").value;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "IsEntityGlowingEvent": {
                if (this.getDrawMode() != MODE_GLOW) break;

                IsEntityGlowingEvent e = (IsEntityGlowingEvent)event;

                if (!shouldRender(e.entity)) break;
                e.cir.setReturnValue(true);

                break;
            }
            case "OnRenderEvent": {
                if (this.getDrawMode() != MODE_MOB_BOX) break;

                OnRenderEvent e = (OnRenderEvent)event;
                Iterable<Entity> ents = CheatClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    // No render myself.
                    if (!this.shouldRender(entity)) {
                        continue;
                    }

                    // Render mob box
                    RenderUtils.renderBox(entity, e.tickDelta, e.mStack, (Boolean)this.getSetting("BlendBoxes").value, (Float)this.getSetting("BoxPadding").value);
                }

                break;
            }
        }
    }
}
