package net.como.client.modules.hud;

import com.mojang.blaze3d.systems.RenderSystem;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.render.InGameHudRenderEvent;
import net.como.client.misc.settings.Setting;
import net.como.client.modules.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Watermark extends Module {
    public static final Identifier WATERMARK_TEXTURE = new Identifier("como-client", "textures/misc/watermark.png");

    public static final int BACKGROUND_WIDTH = 1193;
    public static final int BACKGROUND_HEIGHT = 646;

    public Watermark() {
        super("Watermark");
        this.setDescription("Renders the Como Client watermark on the screen.");

        this.addSetting(new Setting("Scale", 1.0d));
        
        this.setCategory("HUD");
    }
    
    @Override
    public void activate() {
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
    }

    public static void render(MatrixStack matrixStack, Double scale, double x, double y) {
        matrixStack.push();
        matrixStack.translate(x, y, 0);

        RenderSystem.setShaderTexture(0, WATERMARK_TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        // Scaling
        int width  = (int)(BACKGROUND_WIDTH * scale);
        int height = (int)(BACKGROUND_HEIGHT * scale);

        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, width, height, width, height);

        matrixStack.pop();
    }

    public static void render(MatrixStack matrixStack, Double scale) {
        // Scaling
        int width  = (int)(BACKGROUND_WIDTH * scale);
        int height = (int)(BACKGROUND_HEIGHT * scale);

        int x = ComoClient.getClient().getWindow().getScaledWidth() - width;
        int y = ComoClient.getClient().getWindow().getScaledHeight() - height;

        render(matrixStack, scale, x, y);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                render(e.mStack, this.getDoubleSetting("Scale") / 6);

                break;
            }
        }
    }
}
