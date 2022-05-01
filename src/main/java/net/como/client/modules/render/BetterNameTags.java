package net.como.client.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.ComoClient;
import net.como.client.components.ProjectionUtils;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.renderLabelIfPresentEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.maths.Vec3;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class BetterNameTags extends Module {
    private static class Attribute {
        public final PlayerEntity player;

        public int getColour() {
            return 0xFFFFFFFF;
        }

        public Text getText() {
            return Text.of("");
        }

        public Attribute(PlayerEntity player) {
            this.player = player;
        }
    }

    private static class NameAttribute extends Attribute {
        NameAttribute(PlayerEntity player) {
            super(player);
        }

        @Override
        public Text getText() {
            return this.player.getName();
        }
    }

    private static class PingAttribute extends Attribute {
        PingAttribute(PlayerEntity player) {
            super(player);
        }

        private Integer getPing() {
            ClientPlayNetworkHandler lv = ComoClient.me().networkHandler;

            // Get the player entry
            PlayerListEntry entry = lv.getPlayerListEntry(player.getUuid());
            
            // We don't know em so they must be apart of the server right?
            if (entry == null) return 0;

            return entry.getLatency();
        }

        @Override
        public Text getText() {
            return Text.of(String.format("%dms", this.getPing()));
        }

        @Override
        public int getColour() {
            Integer ping = this.getPing(); 

            int badPing = 500;
            float f = (float)ping/(float)badPing * 255;
            if (f > 255) f = 255;

            return RenderUtils.RGBA2Int((int)(2*f), (int)(255 - f), 0, 255);
        }
    }

    private static class HealthAttribute extends Attribute {
        public HealthAttribute(PlayerEntity player) {
            super(player);
        }

        private Integer getHealth() {
            return (int)player.getHealth();
        }

        @Override
        public Text getText() {
            return Text.of(this.getHealth().toString());
        }

        @Override
        public int getColour() {
            float f = (this.getHealth() / player.getMaxHealth()) * 255*2;

            return RenderUtils.RGBA2Int((int)(255*2 - f), (int)(f), 0, 255);
        }
    }

    public BetterNameTags() {
        super("BetterNameTags");

        this.addSetting(new Setting("Scale", 0.5f));
        this.addSetting(new Setting("OutlineAlpha", 125));
        this.addSetting(new Setting("EntityOwner", false));
    
        this.description = "Renders a different kind of name-tag above nearby players.";

        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(InGameHudRenderEvent.class);
        this.addListen(renderLabelIfPresentEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
        this.removeListen(renderLabelIfPresentEvent.class);
    }

    private void displayNameTag(PlayerEntity player, MatrixStack mStack, float tickDelta) {
        // Get position
        Vec3d pos = player.getLerpedPos(tickDelta).add(0, player.getBoundingBox().maxY - player.getPos().y + 0.25, 0);
        
        // Text renderer
        TextRenderer r = ComoClient.textRenderer;

        // Get the different attributes
        float textOffsets = r.getWidth(" ")/2;
        Attribute[] attributes = {
            new NameAttribute(player),
            new HealthAttribute(player),
            new PingAttribute(player)
        };

        // Calculate length
        int len = 0;
        for (Attribute attribute : attributes) {
            len += r.getWidth(attribute.getText());
        }
        len += (attributes.length - 1) * textOffsets;

        // Calculate scale
        float scale = (Float)(this.getSetting("Scale").value);
        float absoluteScale = scale*(float)ProjectionUtils.getScale(pos, tickDelta);

        // WorldToScreen
        Vec3 playerPos = new Vec3(pos);
        if (!ProjectionUtils.to2D(playerPos, absoluteScale)) return;
        ProjectionUtils.scaleProjection(absoluteScale);

        // Positions
		float x = -len/2 + (float)playerPos.x;
		float y = -10 + (float)playerPos.y;
        int outlineAlpha = (int)this.getSetting("OutlineAlpha").value;

        Matrix4f matrix4f = mStack.peek().getPositionMatrix();
        for (Attribute attribute : attributes) {
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            
            r.drawWithOutline(attribute.getText().asOrderedText(), x, y, attribute.getColour(), RenderUtils.RGBA2Int(0, 0, 0, outlineAlpha + 5), matrix4f, immediate, 255);
            x += r.getWidth(attribute.getText()) + textOffsets;
            
            immediate.draw();
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;
                Iterable<Entity> ents = ComoClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    // No render myself.
                    if (!(entity instanceof PlayerEntity) || ((PlayerEntity)entity == ComoClient.me() && !ClientUtils.isThirdperson())) {
                        continue;
                    }

                    PlayerEntity player = (PlayerEntity)entity;
                    
                    this.displayNameTag(player, e.mStack, e.tickDelta);
                    ProjectionUtils.resetProjection();
                }

                break;
            }
            case "renderLabelIfPresentEvent": {
                renderLabelIfPresentEvent<Entity> e = (renderLabelIfPresentEvent<Entity>)event;
                if (!(e.entity instanceof PlayerEntity)) break;

                e.ci.cancel();

                break;
            }
        }
    }
}
