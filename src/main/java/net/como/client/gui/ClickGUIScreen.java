package net.como.client.gui;

import net.como.client.ComoClient;
import net.como.client.modules.hud.ClickGUI;
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
        super.render(matrices, mouseX, mouseY, delta);
        this.clickGUI.renderMenuBlocks(matrices, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.clickGUI.handleClick(button);
    }
}
