package net.como.client.modules.hud;

import com.mojang.blaze3d.systems.RenderSystem;

import net.como.client.ComoClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;

public class Watermark extends Module {
    private static final Identifier WATERMARK_TEXTURE = new Identifier("como-client", "textures/misc/watermark.png");

    private static final int BACKGROUND_WIDTH = 1193;
    private static final int BACKGROUND_HEIGHT = 646;

    public Watermark() {
        super("Watermark", true);

        this.addSetting(new Setting("Scale", 1.0d));

        this.description = "Renders the Como Client watermark on the screen.";
    }
    
    @Override
    public void activate() {
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                RenderSystem.setShaderTexture(0, WATERMARK_TEXTURE);
                RenderSystem.setShaderColor(1, 1, 1, 1);

                // Scaling
                double scale = ((double)this.getSetting("Scale").value)/6;
                int width  = (int)(BACKGROUND_WIDTH * scale);
                int height = (int)(BACKGROUND_HEIGHT * scale);

                int x = ComoClient.getClient().getWindow().getScaledWidth() - width;
                int y = ComoClient.getClient().getWindow().getScaledHeight() - height;

                DrawableHelper.drawTexture(e.mStack, x, y, 0, 0, width, height, width, height);

                break;
            }
        }
    }
}
