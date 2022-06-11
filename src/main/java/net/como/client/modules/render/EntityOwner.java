package net.como.client.modules.render;

import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.render.OnRenderEvent;
import net.como.client.modules.Module;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class EntityOwner extends Module {

    public EntityOwner() {
        super("EntityOwner");

        this.addSetting(new Setting("Scale", 1d));

        this.setDescription("Shows who owns a given pet.");

        this.setCategory("Render");
    }
    
    private void renderName(String name, Vec3d pos, float tickDelta, MatrixStack mStack) {
        GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        RenderSystem.setShader(GameRenderer::getPositionShader);
        
        mStack.push();

        RenderUtils.applyRegionalRenderOffset(mStack);

        mStack.translate(
            (pos.getX()) - RenderUtils.getRegion().getX(),
            pos.getY(),
            (pos.getZ()) - RenderUtils.getRegion().getZ()
        );
        
        // Update the size of the box.
		mStack.multiply(ComoClient.getClient().getEntityRenderDispatcher().getRotation());
		float c = (float)Math.sqrt(ComoClient.getClient().cameraEntity.getLerpedPos(tickDelta).distanceTo(pos));

        TextRenderer r = ComoClient.getInstance().textRenderer;

        float scale = (float)(double)(this.getDoubleSetting("Scale"));
		mStack.scale(-0.025F*c*scale, -0.025F*c*scale, 0);
            
        float x = -r.getWidth(Text.of(name))/2;
		float y = -10;

        r.draw(mStack, Text.of(name), x, y, 0xFFFFFFFF);

        mStack.pop();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
    }

    /**
     * Gets an entities owner id
     * @param entity
     * @return NULL if couldn't be found or the player's UUID
     */
    private UUID getOwnerUuid(Entity entity) {
        UUID ownerUuid = null;

        // TODO add projectiles

        if (entity instanceof TameableEntity) ownerUuid = ((TameableEntity) entity).getOwnerUuid();
        else if (entity instanceof HorseBaseEntity) ownerUuid = ((HorseBaseEntity) entity).getOwnerUuid();

        return ownerUuid;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;
                
                Iterable<Entity> ents = ComoClient.getClient().world.getEntities();

                for (Entity entity : ents) {
                    UUID uuid = this.getOwnerUuid(entity);

                    if (uuid == null) continue;

                    String name = ClientUtils.getPlayerUsername(uuid);
                    this.renderName(name, entity.getLerpedPos(e.tickDelta), e.tickDelta, e.mStack);
                }

                break;
            }
        }
    }
}
