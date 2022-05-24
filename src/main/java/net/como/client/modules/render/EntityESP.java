package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.IsEntityGlowingEvent;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderEntityEvent;
import net.como.client.structures.Module;
import net.como.client.structures.Colour;
import net.como.client.structures.Mode;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;


public class EntityESP extends Module {
    public EntityESP() {
        super("EntityESP");

        // Bounding Boxes
        this.addSetting(new Setting("BoxPadding", 0f));
        this.addSetting(new Setting("BlendBoxes", false));

        // Glow
        this.addSetting(new Setting("GlowColour", false));

        // Drawing Mode setting
        this.addSetting(new Setting("Mode", new Mode("Glow", "Box")));

        this.setDescription("Know where entities are more easily.");

        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(IsEntityGlowingEvent.class);
        this.addListen(RenderEntityEvent.class);
    }

    @Override
	public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(IsEntityGlowingEvent.class);
        this.removeListen(RenderEntityEvent.class);
	}

    private boolean shouldRender(Entity entity) {
        return !(entity instanceof PlayerEntity && (PlayerEntity)entity == ComoClient.me());
    }

    // Just a wrapper for rendering the boxes
    private void renderBoxes(Entity entity, float tickDelta, MatrixStack mStack) {
        RenderUtils.renderBox(entity, tickDelta, mStack, (Boolean)this.getSetting("BlendBoxes").value, (Float)this.getSetting("BoxPadding").value);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            // For entity glow
            case "IsEntityGlowingEvent": {
                if (!this.getModeSetting("Mode").is("Glow")) break;

                IsEntityGlowingEvent e = (IsEntityGlowingEvent)event;
                if (!this.shouldRender(e.entity)) break;

                e.cir.setReturnValue(true);

                break;
            }
            case "RenderEntityEvent": {
                if (!this.getModeSetting("Mode").is("Glow")) break;

                RenderEntityEvent e = (RenderEntityEvent)event;

                // Don't bother if we don't want them.
                if (!this.shouldRender(e.entity)) break;

                // Make sure we have the right vertexConsumers
                if (!(e.vertexConsumers instanceof OutlineVertexConsumerProvider)) {
                    break;
                }

                OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider)(e.vertexConsumers);

                // Calculate what colour we want
                Colour colour = ((Boolean)this.getSetting("GlowColour").value) ? Colour.fromDistance(e.entity) : new Colour(255, 255, 255, 255);

                // Set the colour
                outlineVertexConsumers.setColor((int)colour.r, (int)colour.g, (int)colour.b, (int)colour.a);

                break;
            }
            case "OnRenderEvent": {
                if (!this.getModeSetting("Mode").is("Box")) break;

                OnRenderEvent e = (OnRenderEvent)event;
                Iterable<Entity> ents = ComoClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    // Don't render stuff we don't want to see!
                    if (!this.shouldRender(entity)) {
                        continue;
                    }

                    this.renderBoxes(entity, e.tickDelta, e.mStack);
                }
                break;
            }
        }
    }
}
