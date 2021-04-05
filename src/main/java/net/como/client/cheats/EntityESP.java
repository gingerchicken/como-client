package net.como.client.cheats;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EntityESP extends Cheat {
    public EntityESP() {
        super("Entity ESP");
    }

    private int mobBox;

    @Override
    public void activate() {
        mobBox = GL11.glGenLists(1);
		GL11.glNewList(mobBox, GL11.GL_COMPILE);
		Box bb = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBox(bb);
		GL11.glEndList();
    }

    @Override
	public void deactivate()
	{
		GL11.glDeleteLists(mobBox, 1);
		mobBox = 0;
	}

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderEntity": {
                Entity entity   = (Entity)args[0];

                double cameraX  = (double)args[1];
                double cameraY  = (double)args[2];
                double cameraZ  = (double)args[3];
                float tickDelta = (float) args[4];
                
                MatrixStack matrices = (MatrixStack)args[5];
                VertexConsumerProvider vertexConsumers = (VertexConsumerProvider)args[6];
                
                Box boundingBox = entity.getVisibilityBoundingBox();

                // GL settings
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glLineWidth(2);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LIGHTING);
                
                GL11.glPushMatrix();
                RenderUtils.applyRegionalRenderOffset();
                
                BlockPos camPos = RenderUtils.getCameraBlockPos();
                int regionX = (camPos.getX() >> 9) * 512;
                int regionZ = (camPos.getZ() >> 9) * 512;
                
                // if(style.getSelected().boxes)
                renderBoxes(entity, tickDelta, regionX, regionZ);
                
                // if(style.getSelected().lines)
                //     renderTracers(partialTicks, regionX, regionZ);
                
                GL11.glPopMatrix();
                
                // GL resets
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);

                // TODO make esp box or something lol
            }
        }
    }

    private void renderBoxes(Entity e, double partialTicks, int regionX, int regionZ)
	{
        // partialTicks = 0;
        // System.out.println(partialTicks);
		double extraSize = 0;// boxSize.getSelected().extraSize;

        GL11.glPushMatrix();
        
        ClientPlayerEntity me = CheatClient.me();
        
        Vec3d whackPosEntity = RenderUtils.whackifyPos(e, regionX, regionZ, partialTicks);
        Vec3d whackPosMe     = RenderUtils.whackifyPos(CheatClient.me(), regionX, regionZ, partialTicks);

        // RenderUtils.drawArrow(new Vec3d(e.prevX + (e.getX() - e.prevX) * partialTicks - regionX,
        // e.prevY + (e.getY() - e.prevY) * partialTicks,
        // e.prevZ + (e.getZ() - e.prevZ) * partialTicks - regionZ), new Vec3d(me.prevX + (me.getX() - me.prevX) * partialTicks - regionX,
        // me.prevY + (me.getY() - me.prevY) * partialTicks,
        // me.prevZ + (me.getZ() - me.prevZ) * partialTicks - regionZ));

        GL11.glTranslated(whackPosEntity.x, whackPosEntity.y, whackPosEntity.z);
        
        GL11.glScaled(e.getWidth() + extraSize, e.getHeight() + extraSize,
            e.getWidth() + extraSize);
        
        float f = CheatClient.me().distanceTo(e) / 20F;
        GL11.glColor4f(2 - f, f, 0, 0.5F);
        
        GL11.glCallList(mobBox);
        
        GL11.glPopMatrix();
		
	}
}
