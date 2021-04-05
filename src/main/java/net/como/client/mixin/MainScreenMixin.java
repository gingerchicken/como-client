package net.como.client.mixin;

import net.como.client.screens.ComoClientOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MainScreenMixin extends Screen {
	public MainScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At("RETURN"), method = "initWidgetsNormal(II)V")
	private void onInitWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
		// y is just the position of the centre of the buttons
		// Spacing is the spacing :V

		addButton(new ButtonWidget(width / 2 + 2, y + spacingY * 2, 98, 20,
			new LiteralText("Como Client Options"),
			b -> {
				client.openScreen(new ComoClientOptions(this));
			}));
		
		// Make sure that the other button is spaced properly.
		for(AbstractButtonWidget button : buttons) {
			if(button.getMessage().getString().equals(I18n.translate("menu.online"))) {
				// Set the width.
				button.setWidth(button.getWidth() / 2);
			}
		}
	}
}
