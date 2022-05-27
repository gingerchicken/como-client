package net.como.client.modules.hud;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.render.InGameHudRenderEvent;
import net.como.client.events.render.RenderHealthBarEvent;
import net.como.client.misc.Colour;
import net.como.client.misc.attributes.entity.HealthAttribute;
import net.como.client.modules.Module;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.Render2DUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class MinifiedHealth extends Module {
    private final TextRenderer textRenderer = ComoClient.getClient().textRenderer;

    public MinifiedHealth() {
        super("MinifiedHealth");

        this.setDescription("Makes the health bar turn into a number");

        this.setCategory("HUD");
    }

    @Override
    public void activate() {
        this.addListen(RenderHealthBarEvent.class);
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
        this.removeListen(RenderHealthBarEvent.class);
    }

    private void renderHealthBar(MatrixStack matrices, int x, int y) {
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        ComoClient.getClient().textRenderer.drawWithOutline(
            this.getHealth().asOrderedText(),
            (float)x, (float)y, RenderUtils.RGBA2Int(new Colour(255, 19, 19, 255)), RenderUtils.RGBA2Int(new Colour(0, 0, 0, 150)), matrices.peek().getPositionMatrix(), immediate, 255
        );
        
        immediate.draw();
    }

    private Text getHealth() {
        return (new HealthAttribute(ComoClient.me())).getText();
    }

    @Override
    public void fireEvent(Event event) {
        // Ignore if we're not in survival or adventure mode
        GameMode mode = ClientUtils.getGameMode();
        if (mode != GameMode.SURVIVAL && mode != GameMode.ADVENTURE) return;

        // Handle the events
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                this.renderHealthBar(e.mStack, ComoClient.getClient().getWindow().getScaledWidth() / 2 - 91, ComoClient.getClient().getWindow().getScaledHeight() - 38);

                break;
            }
            case "RenderHealthBarEvent": {
                RenderHealthBarEvent e = (RenderHealthBarEvent)event;
                e.ci.cancel();

                Render2DUtils.renderHeart(e.matrices, e.x + textRenderer.getWidth(this.getHealth()) + 2, e.y);

                break;
            }
        }
    }
}
