package net.como.client.gui;

import io.netty.util.internal.MathUtil;
import net.como.client.ComoClient;
import net.como.client.modules.hud.ClickGUI;
import net.como.client.structures.Colour;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ClickGUIScreen extends Screen {

    private ClickGUI clickGUI;

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
        
        this.fillGradient(matrices, 0, 0, this.width, this.height, RenderUtils.RGBA2Int(new Colour(15, 15, 15, 150f * lerpedBackFade)), RenderUtils.RGBA2Int(new Colour(0, 0, 0, 125f * lerpedBackFade)));
    }
}
