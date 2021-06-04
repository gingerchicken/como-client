package net.como.client.cheats;

import org.lwjgl.opengl.GL11;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;

import net.como.client.utils.RenderUtils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EntityESP extends Cheat {
    public EntityESP() {
        super("Entity ESP");

        this.settings.addSetting(new Setting("BoundingBox", true));
        this.settings.addSetting(new Setting("BoxPadding", 0d));

        this.description = "Know where entities are more easily.";
    }

    private int mobBox;

    @Override
    public void activate() {
        // TODO Learn what this means
        
        mobBox = GL11.glGenLists(1);
		GL11.glNewList(mobBox, GL11.GL_COMPILE);

		Box bb = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBox(bb);

		GL11.glEndList();
    }

    @Override
	public void deactivate() {
		GL11.glDeleteLists(mobBox, 1);
		mobBox = 0;
	}

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            // TODO maybe this won't render entities if they are not rendered?
            case "onRenderEntity": {
                // Get Arguments
                Entity entity   = (Entity)args[0];
                float tickDelta = (float) args[4];

                // GL settings
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glLineWidth(2);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LIGHTING);
                
                // Render Section
                GL11.glPushMatrix();
                RenderUtils.applyRegionalRenderOffset();
                
                BlockPos camPos = RenderUtils.getCameraBlockPos();
                int regionX = (camPos.getX() >> 9) * 512;
                int regionZ = (camPos.getZ() >> 9) * 512;
                
                // Get settings
                Boolean drawBoundingBox = (Boolean)this.settings.getSetting("BoundingBox").value;

                // Check the settings
                if (drawBoundingBox) this.renderBox(entity, tickDelta, regionX, regionZ);

                // Pop the stack
                GL11.glPopMatrix();
                
                // GL resets
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
            }
        }
    }

    private void renderBox(Entity e, double partialTicks, int regionX, int regionZ) {
        // Get our extraSize setting
		Double extraSize = (Double) this.settings.getSetting("BoxPadding").value;

        // Push a new item to the render stack
        GL11.glPushMatrix();

        // Get the whacky position of the entity
        Vec3d whackPosEntity = RenderUtils.whackifyPos(e, regionX, regionZ, partialTicks);

        // Set the centre of the box there (I believe could be wrong...)
        GL11.glTranslated(whackPosEntity.x, whackPosEntity.y, whackPosEntity.z);
        
        // Update the size of the box.
        GL11.glScaled(e.getWidth() + extraSize, e.getHeight() + extraSize, e.getWidth() + extraSize);
        
        // Make the boxes change colour depending on their distance.
        float f = CheatClient.me().distanceTo(e) / 20F;
        GL11.glColor4f(2 - f, f, 0, 0.5F);
        
        // Make it so it is our mobBox.
        GL11.glCallList(mobBox);
        
        // Pop the stack (i.e. render it)
        GL11.glPopMatrix();
		
	}
}
