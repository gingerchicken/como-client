package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.components.ProjectionUtils;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.render.InGameHudRenderEvent;
import net.como.client.misc.Colour;
import net.como.client.misc.attributes.Attribute;
import net.como.client.misc.attributes.entity.ActiveItemAttribute;
import net.como.client.misc.attributes.entity.ArmourAttribute;
import net.como.client.misc.attributes.entity.HealthAttribute;
import net.como.client.misc.attributes.entity.NameAttribute;
import net.como.client.misc.maths.Box2D;
import net.como.client.misc.maths.Vec3;
import net.como.client.modules.Module;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.Render2DUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class x88ESP extends Module {

    public x88ESP() {
        super("x88ESP");

        this.setDescription("See players with a x88 lookalike box.");

        this.setCategory("Render");

        this.addSetting(new Setting("LookLength", 3d) {{
            this.setMin(0d);
            this.setMax(10d);

            this.setDescription("The length of the angle tracer.");
        }});
    }
    
    @Override
    public void activate() {
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
    }

    private void renderx88Box(LivingEntity entity, float tickDelta, MatrixStack mStack) {
        // Rendering the 2D bounding box
        Box2D box = Box2D.fromEntity(entity, tickDelta);

        if (box == null) return;

        Vec3 max = box.max;
        Vec3 min = box.min;

        // Render the box
        Render2DUtils.renderBox(mStack, (int)min.x, (int)min.y, (int)max.x, (int)max.y, Colour.fromHealth(entity));
        Render2DUtils.renderBox(mStack, (int)min.x - 1, (int)min.y - 1, (int)max.x + 1, (int)max.y + 1);

        // Rendering the look tracer
        // Get the look length setting
        double lookLength = this.getDoubleSetting("LookLength");

        // Render where they're looking
        Vec3d eyePosOffset = entity.getEyePos().subtract(entity.getPos());
        Vec3d eyePos = entity.getLerpedPos(tickDelta).add(eyePosOffset);
        Vec3d endPos = MathsUtils.getForwardVelocity(entity).multiply(lookLength).add(eyePos);

        // Project the positions
        Vec3 projectedEyePos = new Vec3(eyePos);
        Vec3 projectedEndPos = new Vec3(endPos);

        // Project the positions to 2D
        if (ProjectionUtils.getInstance().to2D(projectedEndPos, 1) && ProjectionUtils.getInstance().to2D(projectedEyePos, 1)) {
            // Render the line
            Render2DUtils.renderLine(mStack, (int)projectedEyePos.x, (int)projectedEyePos.y, (int)projectedEndPos.x, (int)projectedEndPos.y);
        }

        // Render the attributes
        int textHeight = 10;

        // The positions on the box
        Vec3 bottomPos = new Vec3(max.x - 2, max.y+5, 0);
        Vec3 topPos    = new Vec3(min.x, min.y - textHeight, 0);

        // We're gonna get the retro-looking text renderer
        TextRenderer textRenderer = ComoClient.getClient().textRenderer;

        // Bottom Attributes
        Attribute bottomAttributes[] = {
            new HealthAttribute(entity) {
                @Override
                public Text getText() {
                    return Text.of("HP: " + super.getText().asString());
                }
            },
            new ArmourAttribute(entity) {
                @Override
                public Text getText() {
                    return Text.of("Armour: " + super.getText().asString());
                }
            },
            new ActiveItemAttribute(entity) {
                @Override
                public Text getText() {
                    if (this.getEntity().getMainHandStack().getItem() == Items.AIR) return Text.of("");

                    return super.getText();
                }
            },
        };

        // Top Attributes
        Attribute topAttributes[] = {
            new NameAttribute(entity)
        };

        // Render the bottom attributes
        for (Attribute attribute : bottomAttributes) {
            Text text = attribute.getText();

            textRenderer.draw(mStack, text, (int)bottomPos.x, (int)bottomPos.y, attribute.getColour());

            if (!text.asString().isBlank()) bottomPos.add(0, textHeight, 0);
        }

        // Render the top attributes
        for (Attribute attribute : topAttributes) {
            textRenderer.draw(mStack, attribute.getText(), (int)topPos.x, (int)topPos.y, attribute.getColour());

            topPos.add(0, -textHeight, 0);
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                ProjectionUtils.unscaledProjection();

                // Get all of the entities
                Iterable<Entity> ents = ComoClient.getClient().world.getEntities();

                // Render the entities
                for (Entity entity : ents) {
                    // Make sure it is a player
                    if (!(entity instanceof PlayerEntity)) {
                        continue;
                    }

                    // Check if the player is us
                    if (entity == ComoClient.me()) {
                        // Check if we're in thirdperson
                        if (!ClientUtils.isThirdperson()) continue;
                    }

                    // Get tick delta
                    float tickDelta = ((InGameHudRenderEvent)event).tickDelta;

                    // Get the matrix stack
                    MatrixStack mStack = ((InGameHudRenderEvent)event).mStack;

                    // Render the entity
                    this.renderx88Box((LivingEntity)entity, tickDelta, mStack);
                }

                ProjectionUtils.resetProjection();
                break;
            }
        }
    }
}
