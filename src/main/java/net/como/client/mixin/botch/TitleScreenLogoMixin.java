package net.como.client.mixin.botch;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Mixin(TitleScreen.class)
public class TitleScreenLogoMixin extends Screen {

    protected TitleScreenLogoMixin(Text title) {
        super(title);
    }


    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (ComoClient.getInstance().getModules().get("hidetitlemessage").isEnabled()) return;

        MinecraftClient client = ComoClient.getClient();
        
        TextRenderer tr = client.textRenderer;
        String text = "Thank you for using Como Client!";

        int padding = 2;
        int x = client.getWindow().getScaledWidth() - tr.getWidth(text) - padding;
        int y = ComoClient.isMeteorLoaded() ? 12 + padding : padding;

        tr.drawWithShadow(matrices, Text.of(text), x, y, RenderUtils.RGBA2Int(0, 255, 0, 255));
    }
}
