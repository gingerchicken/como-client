package net.como.client.mixin.botch.runewaredupe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.modules.exploits.RunewareDupe;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;

@Mixin(GenericContainerScreen.class)
public class EnderChestScreenMixin extends Screen {
    protected EnderChestScreenMixin(Text title) {
        super(title);
        //TODO Auto-generated constructor stub
    }

    ButtonWidget buttonWidget = null;
    
    private RunewareDupe RunewareDupe() {
        return (RunewareDupe)(ComoClient.Modules.get("runewaredupe"));
    }

    // It didn't like init sooo
    private void renderDupeButton() {
        if (this.buttonWidget != null) this.remove(buttonWidget);;

        Text buttonText = Text.of("Dupe");

        double padding = 5;
        double width = this.textRenderer.getWidth(buttonText) + padding*2;
        double height = 20;

        double x = ComoClient.getClient().getWindow().getScaledWidth() / 2 - width/2;
        double y = ((this.height - 166) / 2) - height - padding;

        this.buttonWidget = this.addDrawableChild(new ButtonWidget((int)x, (int)y, (int)width, (int)height, buttonText, (button) -> {
            this.RunewareDupe().performDupe = true;
        }));

        this.buttonWidget.active = RunewareDupe().shouldActivateButton();
    }

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // TODO check if end chest

        RunewareDupe RunewareDupe = this.RunewareDupe();
        
        // Make sure that the dupe is enabled.
        if (!RunewareDupe.isEnabled()) return;

        this.renderDupeButton();
    }
}
