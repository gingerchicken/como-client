package net.como.client.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.ComoClient;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.renderLabelIfPresentEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
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
        this.addListen(OnRenderEvent.class);
        this.addListen(renderLabelIfPresentEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(renderLabelIfPresentEvent.class);
    }

    private void displayNameTag(PlayerEntity player, MatrixStack mStack, float tickDelta) {
        Vec3d pos = player.getLerpedPos(tickDelta).add(0, player.getBoundingBox().maxY - player.getPos().y + 0.25, 0);
        TextRenderer r = ComoClient.textRenderer;

        float textOffsets = r.getWidth(" ")/2;
        Attribute[] attributes = {
            new NameAttribute(player),
            new HealthAttribute(player),
            new PingAttribute(player)
        };

        int len = 0;
        for (Attribute attribute : attributes) {
            len += r.getWidth(attribute.getText());
        }
        len += (attributes.length - 1) * textOffsets;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		
        // Load the renderer
        RenderSystem.setShader(GameRenderer::getPositionShader);

        // Push a new item to the render stack
        mStack.push();

        // Apply
        RenderUtils.applyRegionalRenderOffset(mStack);

        // Translate the point of rendering
        mStack.translate(
            (pos.getX()) - RenderUtils.getRegion().getX(),
            pos.getY(),
            (pos.getZ()) - RenderUtils.getRegion().getZ()
        );
        
        // Update the size of the box.
		mStack.multiply(ComoClient.getClient().getEntityRenderDispatcher().getRotation());
		float c = (float)Math.sqrt(ComoClient.getClient().cameraEntity.getLerpedPos(tickDelta).distanceTo(pos));

        float scale = (Float)(this.getSetting("Scale").value);
		mStack.scale(-0.025F*c*scale, -0.025F*c*scale, 0);
        Matrix4f matrix4f = mStack.peek().getPositionMatrix();

		float x = -len/2;
		float y = -10;
        int outlineAlpha = (int)this.getSetting("OutlineAlpha").value;

        for (Attribute attribute : attributes) {
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            
            r.drawWithOutline(attribute.getText().asOrderedText(), x, y, attribute.getColour(), RenderUtils.RGBA2Int(0, 0, 0, outlineAlpha + 5), matrix4f, immediate, 255);
            x += r.getWidth(attribute.getText()) + textOffsets;
            
            immediate.draw();
        }

        // Pop the stack (i.e. render it)
        mStack.pop();

        // GL resets
        // RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;
                Iterable<Entity> ents = ComoClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    // No render myself.
                    if (!(entity instanceof PlayerEntity) || ((PlayerEntity)entity == ComoClient.me() && !ClientUtils.isThirdperson())) {
                        continue;
                    }

                    PlayerEntity player = (PlayerEntity)entity;

                    this.displayNameTag(player, e.mStack, e.tickDelta);
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
