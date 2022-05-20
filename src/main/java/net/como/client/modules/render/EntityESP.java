package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.components.ProjectionUtils;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.IsEntityGlowingEvent;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderEntityEvent;
import net.como.client.structures.Module;
import net.como.client.structures.Colour;
import net.como.client.structures.Mode;
import net.como.client.structures.events.Event;
import net.como.client.structures.maths.Box2D;
import net.como.client.structures.maths.Vec3;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.Render2DUtils;
import net.como.client.utils.RenderUtils;
import net.como.client.utils.Render2DUtils.BufferContainer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;


public class EntityESP extends Module {
    public EntityESP() {
        super("EntityESP");

        // Bounding Boxes
        this.addSetting(new Setting("BoxPadding", 0f));
        this.addSetting(new Setting("BlendBoxes", false));

        // Glow
        this.addSetting(new Setting("GlowColour", false));

        // x88
        this.addSetting(new Setting("x88LookLength", 5d));

        // Drawing Mode setting
        this.addSetting(new Setting("Mode", new Mode("Glow", "Box", "x88")));

        this.description = "Know where entities are more easily.";

        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(IsEntityGlowingEvent.class);
        this.addListen(RenderEntityEvent.class);
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
	public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(IsEntityGlowingEvent.class);
        this.removeListen(RenderEntityEvent.class);
        this.removeListen(InGameHudRenderEvent.class);
	}

    private boolean shouldRender(Entity entity) {
        return !(entity instanceof PlayerEntity && (PlayerEntity)entity == ComoClient.me());
    }

    // Just a wrapper for rendering the boxes
    private void renderBoxes(Entity entity, float tickDelta, MatrixStack mStack) {
        RenderUtils.renderBox(entity, tickDelta, mStack, (Boolean)this.getSetting("BlendBoxes").value, (Float)this.getSetting("BoxPadding").value);
    }

    private void renderx88Box(Entity entity, float tickDelta, MatrixStack mStack) {
        // Rendering the 2D bounding box
        Box2D box = Box2D.fromEntity(entity, tickDelta);

        if (box == null) return;

        Vec3 max = box.max;
        Vec3 min = box.min;

        // Render the box
        Render2DUtils.renderBox(mStack, (int)min.x, (int)min.y, (int)max.x, (int)max.y);

        // Rendering the look tracer
        // Get the look length setting
        double lookLength = this.getDoubleSetting("x88LookLength");

        // Render where they're looking
        Vec3d eyePos = entity.getEyePos();
        Vec3d endPos = MathsUtils.getForwardVelocity(entity).multiply(lookLength).add(eyePos);

        // Project the positions
        Vec3 projectedEyePos = new Vec3(eyePos);
        Vec3 projectedEndPos = new Vec3(endPos);

        // Project the positions to 2D
        if (ProjectionUtils.to2D(projectedEndPos, 1) && ProjectionUtils.to2D(projectedEyePos, 1)) {
            // Render the line
            Render2DUtils.renderLine(mStack, (int)projectedEyePos.x, (int)projectedEyePos.y, (int)projectedEndPos.x, (int)projectedEndPos.y);
        }

        // Render the bones
        // Get the model
        // Find the bones
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
            case "InGameHudRenderEvent": {
                if (!this.getModeSetting("Mode").is("x88")) break;

                ProjectionUtils.unscaledProjection();

                // Get all of the entities
                Iterable<Entity> ents = ComoClient.getClient().world.getEntities();

                // Render the entities
                for (Entity entity : ents) {
                    // Don't render stuff we don't want to see!
                    if (!this.shouldRender(entity)) {
                        continue;
                    }

                    // Get tick delta
                    float tickDelta = ((InGameHudRenderEvent)event).tickDelta;

                    // Get the matrix stack
                    MatrixStack mStack = ((InGameHudRenderEvent)event).mStack;

                    // Render the entity
                    this.renderx88Box(entity, tickDelta, mStack);
                }

                ProjectionUtils.resetProjection();
                break;
            }
        }
    }
}
