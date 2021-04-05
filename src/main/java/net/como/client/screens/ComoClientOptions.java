package net.como.client.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ComoClientOptions extends Screen {
    private final Screen previousScreen;

    public ComoClientOptions(Screen previousScreen) {
        super(new LiteralText("Como Client Options"));
        
        this.previousScreen = previousScreen;
    }

    @Override
    public void init() {
        addButton(new ButtonWidget(width / 2 - 250 / 2, height - 100, 250, 20, new LiteralText("Done"), b -> client.openScreen(this.previousScreen)));
    }

    @Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
        
		// title text
		drawCenteredString(matrixStack, textRenderer, "Como Client Options", width / 2, 15, 0xFFFFFF);
		
        
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
