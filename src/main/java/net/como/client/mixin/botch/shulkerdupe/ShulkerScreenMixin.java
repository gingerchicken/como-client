package net.como.client.mixin.botch.shulkerdupe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.modules.exploits.ShulkerDupe;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Mixin(ShulkerBoxScreen.class)
public class ShulkerScreenMixin extends Screen {
    ButtonWidget buttonWidget = null;

    protected ShulkerScreenMixin(Text title) {
        super(title);
    }

    private ShulkerDupe ShulkerDupe() {
        return (ShulkerDupe)(ComoClient.Modules.get("shulkerdupe"));
    }

    // It didn't like init sooo
    private void renderDupeButton() {
        if (this.buttonWidget != null) this.remove(buttonWidget);;

        Text buttonText = this.ShulkerDupe().getDupeButtonText();

        double padding = 5;
        double width = this.textRenderer.getWidth(buttonText) + padding*2;
        double height = 20;

        double x = ComoClient.getClient().getWindow().getScaledWidth() / 2 - width/2;
        double y = ((this.height - 166) / 2) - height - padding;

        this.buttonWidget = this.addDrawableChild(new ButtonWidget((int)x, (int)y, (int)width, (int)height, buttonText, (button) -> {
            this.ShulkerDupe().performDupe = true;
        }));

        this.buttonWidget.active = ShulkerDupe().shouldActivateButton();
    }

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ShulkerDupe shulkerDupe = this.ShulkerDupe();
        
        // Make sure that the dupe is enabled.
        if (!shulkerDupe.isEnabled()) return;

        this.renderDupeButton();
    }
}
