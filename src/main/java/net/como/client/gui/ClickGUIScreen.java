package net.como.client.gui;

import java.util.Random;

import io.netty.util.internal.MathUtil;
import net.como.client.ComoClient;
import net.como.client.modules.hud.ClickGUI;
import net.como.client.modules.hud.Watermark;
import net.como.client.structures.Colour;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public class ClickGUIScreen extends Screen {
    private ClickGUI clickGUI;

    // Eric's funny
    private static class BouncyWatermark {
        Vec2f pos = Vec2f.ZERO;
        Vec2f velocity = new Vec2f(-1, 1);

        int screenWidth, screenHeight;
        int width, height;

        public void syncSettings() {
            ClickGUI clickGUI = (ClickGUI)ComoClient.Modules.get("clickgui");
            Double bouncySpeed = Math.abs(clickGUI.getDoubleSetting("BouncySpeed"));

            Double signX = (double) (this.velocity.x >= 0 ? 1 : -1);
            Double signY = (double) (this.velocity.y >= 0 ? 1 : -1);

            this.velocity = new Vec2f((float)(signX * bouncySpeed), (float)(signY * bouncySpeed));
        }

        public void tick() {
            // Sync settings
            this.syncSettings();

            // Bouncy Felix!
            if (this.pos.x <= 0 || this.pos.x >= this.screenWidth || this.pos.x + this.width >= this.screenWidth || this.pos.x + this.width <= 0) {
                velocity = new Vec2f(-velocity.x, velocity.y);
            }

            if (this.pos.y <= 0 || this.pos.y >= this.screenHeight || this.pos.y + this.height >= this.screenHeight || this.pos.y + this.height <= 0) {
                velocity = new Vec2f(velocity.x, -velocity.y);
            }

            this.pos = this.pos.add(velocity);
        }

        public void render(MatrixStack matrices, float partialTicks) {
            Watermark.render(matrices, 1d/6, (int)this.pos.x, (int)this.pos.y);
        }

        public BouncyWatermark(int screenWidth, int screenHeight) {
            Random random = new Random();

            this.screenWidth  = screenWidth;
            this.screenHeight = screenHeight;

            this.width  = Watermark.BACKGROUND_WIDTH / 6;
            this.height = Watermark.BACKGROUND_HEIGHT / 6;

            this.pos = new Vec2f(
                random.nextInt(0, this.screenWidth - this.width),
                random.nextInt(0, this.screenHeight - this.height)
            );
        }
    }

    BouncyWatermark bouncyWatermark;

    @Override
    protected void init() {
        super.init();

        this.bouncyWatermark = new BouncyWatermark(this.width, this.height);
    }

    public ClickGUIScreen(ClickGUI clickGUI) {
        super(Text.of("ClickGUI"));

        this.clickGUI = clickGUI;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        this.clickGUI.disable();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices, delta);
        super.render(matrices, mouseX, mouseY, delta);
        this.clickGUI.renderMenuBlocks(matrices, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.clickGUI.handleClick(button);
    }

    private float backgroundFade = 0.0f;
    private float fadeStep = 0.1f;

    @Override
    public void tick() {
        if (backgroundFade < 1.0f) {
            this.backgroundFade += fadeStep;
        }

        if (this.clickGUI.getBoolSetting("Bouncy")) this.bouncyWatermark.tick();
    }

    private float lerp(float curr, float next, float delta) {
        float g = 1.0f - delta;

        return curr * g + next * delta;
    }

    private float getLerpedBackgroundFade(float delta) {
        float next = this.backgroundFade < 1.0f ? this.backgroundFade + this.fadeStep : this.backgroundFade;

        return this.lerp(this.backgroundFade, next, delta);
    }

    public void renderBackground(MatrixStack matrices, float partialTicks) {
        float lerpedBackFade = this.getLerpedBackgroundFade(partialTicks);

        if (this.clickGUI.getBoolSetting("Bouncy")) this.bouncyWatermark.render(matrices, partialTicks);

        this.fillGradient(matrices, 0, 0, this.width, this.height, RenderUtils.RGBA2Int(new Colour(15, 15, 15, 150f * lerpedBackFade)), RenderUtils.RGBA2Int(new Colour(0, 0, 0, 125f * lerpedBackFade)));
    }
}
