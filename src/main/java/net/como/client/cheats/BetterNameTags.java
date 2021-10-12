package net.como.client.cheats;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import io.netty.util.internal.MathUtil;
import net.como.client.CheatClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderEntityEvent;
import net.como.client.events.RenderWorldEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class BetterNameTags extends Cheat {
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

    public BetterNameTags() {
        super("BetterNameTags");
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
    }

    private void displayNameTag(PlayerEntity player, MatrixStack mStack, float tickDelta) {
        Vec3d pos = player.getLerpedPos(tickDelta).multiply(1, 0, 1).add(0, player.getBoundingBox().maxY + 0.25, 0);
        TextRenderer r = CheatClient.getClient().textRenderer;

        float textOffsets = 2.5f;
        HashMap<Text, Integer> textsWithColours = new HashMap<Text, Integer>();

        textsWithColours.put(player.getDisplayName(), 0xFFFFFFFF);
        textsWithColours.put(Text.of("420ms"), 0xFF00FF00);

        int len = 0;
        for (Text text : textsWithColours.keySet()) {
            len += r.getWidth(text) + textOffsets;
        }

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
		mStack.multiply(CheatClient.getClient().getEntityRenderDispatcher().getRotation());
		float c = (float)Math.sqrt(CheatClient.me().getLerpedPos(tickDelta).distanceTo(pos));

		mStack.scale(-0.025F*c, -0.025F*c, 0.025F*c);
        Matrix4f matrix4f = mStack.peek().getModel();

		float x = -len/2;
		float y = -10;

        for (Text text : textsWithColours.keySet()) {
            x = r.drawWithShadow(mStack, text, x + textOffsets, y, textsWithColours.get(text));
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
                Iterable<Entity> ents = CheatClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    // No render myself.
                    if (!(entity instanceof PlayerEntity) || (PlayerEntity)entity == CheatClient.me()) {
                        continue;
                    }

                    PlayerEntity player = (PlayerEntity)entity;

                    this.displayNameTag(player, e.mStack, e.tickDelta);
                }

                break;
            }
        }
    }
}
