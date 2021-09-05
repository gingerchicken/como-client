package net.como.client.cheats;

import org.lwjgl.opengl.GL11;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;

import net.como.client.utils.RenderUtils;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

public class EntityESP extends Cheat {
    public EntityESP() {
        super("EntityESP");

        this.addSetting(new Setting("BoundingBox", true));
        this.addSetting(new Setting("BoxPadding", 0d));
        this.addSetting(new Setting("BlendBoxes", false));

        this.description = "Know where entities are more easily.";
    }

    private VertexBuffer mobBox;

    @Override
    public void activate() {
        mobBox = new VertexBuffer();
		Box bb = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBox(bb, mobBox);
    }

    @Override
	public void deactivate() {
		if (mobBox != null) mobBox.close();
	}

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            // TODO maybe this won't render entities if they are not rendered?
            case "onRenderEntity": {
                // Get Arguments
                Entity entity   = (Entity)args[0];
                float tickDelta = (float) args[4];
                MatrixStack mStack = (MatrixStack)args[5];

                // Settings
                Boolean drawBoundingBox = (Boolean)this.getSetting("BoundingBox").value;
                Boolean blendBoxes = (Boolean)this.getSetting("BlendBoxes").value;

                // GL settings
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                // GL Blending settings
                if (blendBoxes) {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                }
                
                // Render Section
                mStack.push();
                RenderUtils.applyRegionalRenderOffset(mStack);
                
                BlockPos camPos = RenderUtils.getCameraBlockPos();
                int regionX = (camPos.getX() >> 9) * 512;
                int regionZ = (camPos.getZ() >> 9) * 512;

                // Check the settings
                if (drawBoundingBox) this.renderBox(entity, tickDelta, regionX, regionZ, mStack);

                // Pop the stack
                mStack.pop();
                
                // GL resets
                RenderSystem.setShaderColor(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);

                // Blending related stuff
                if (blendBoxes) {
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
        }
    }

    private void renderBox(Entity e, double partialTicks, int regionX, int regionZ, MatrixStack mStack) {
        // Get our extraSize setting
		Float extraSize = (float)(double) this.getSetting("BoxPadding").value;

        // Load the renderer
        RenderSystem.setShader(GameRenderer::getPositionShader);

        // Push a new item to the render stack
        mStack.push();

        // Translate the point of rendering
        mStack.translate(
            e.prevX + (e.getX() - e.prevX) * partialTicks - regionX,
            e.prevY + (e.getY() - e.prevY) * partialTicks,
            e.prevZ + (e.getZ() - e.prevZ) * partialTicks - regionZ
        );
        
        // Update the size of the box.
        mStack.scale(e.getWidth() + extraSize, e.getHeight() + extraSize, e.getWidth() + extraSize);

        // Make the boxes change colour depending on their distance.
        float f = CheatClient.me().distanceTo(e) / 20F;
        RenderSystem.setShaderColor(2 - f, f, 0, 0.5F);
        
        // Make it so it is our mobBox.
        Shader shader = RenderSystem.getShader();
        Matrix4f matrix4f = RenderSystem.getProjectionMatrix();
        mobBox.setShader(mStack.peek().getModel(), matrix4f, shader);
        
        // Pop the stack (i.e. render it)
        mStack.pop();
		
	}
}
