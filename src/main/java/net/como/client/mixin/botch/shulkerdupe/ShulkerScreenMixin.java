package net.como.client.mixin.botch.shulkerdupe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.modules.ShulkerDupe;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Mixin(ShulkerBoxScreen.class)
public class ShulkerScreenMixin extends Screen {

    protected ShulkerScreenMixin(Text title) {
        super(title);
    }
    
    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ShulkerDupe shulkerDupe = (ShulkerDupe)(ComoClient.Cheats.get("shulkerdupe"));
        
        double width = 50;
        double height = 20;

        double x = ComoClient.getClient().getWindow().getScaledWidth() / 2 - width/2;
        double y = 32/ComoClient.getClient().getWindow().getScaleFactor();

        this.addDrawableChild(new ButtonWidget((int)x, (int)y, (int)width, (int)height, Text.of("Dupe"), (button) -> {
            shulkerDupe.performDupe = true;
        }));

        x += width;

    }
}
