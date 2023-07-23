package net.como.client.mixin.botch.autoreconnect;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.modules.utilities.AutoReconnect;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(DisconnectedScreen.class)
public class DisconnectScreenMixin extends Screen {
    private AutoReconnect _autoReconnect;
    private ButtonWidget reconnectButton;

    private AutoReconnect getAutoReconnect() {
        // Make sure that it isn't null
        if (this._autoReconnect == null) {
            this._autoReconnect = (AutoReconnect)ComoClient.getInstance().getModules().get("autoreconnect");
        } 

        // Find it
        return this._autoReconnect;
    }

    // Get private variables
    @Shadow
	@Final
	private Screen parent;

    public DisconnectScreenMixin(Text title) {
        super(title);
    }

    @Override
    public void tick() {
        // Make sure that we are should display
        if (!this.getAutoReconnect().isEnabled()) return;

        // Make sure AutoReconnect is enabled and not manual reconnect
        if (this.isManualMode()) return;

        reconnectButton.setMessage(
            // We add one to the value instead of math.ceil'ing it.
            Text.of(String.format("AutoReconnect (%d)", (int)(this.getAutoReconnect().workCountdown(this.parent)) + 1))
        );
    }

    private boolean isManualMode() {
        return (boolean)this.getAutoReconnect().getSetting("Manual").value;
    }

    @Inject(at = @At("TAIL"), method="init()V", cancellable = false) 
    public void init(CallbackInfo ci) {
        if (!this.getAutoReconnect().isEnabled()) return;

        this.getAutoReconnect().startCountdown();

        // Approximate the reason height
        // TODO just add the damn thing as a widget
        int reasonHeight = 50;

        int backButtonX = width / 2 - 100;
		int backButtonY = Math.min(height / 2 + reasonHeight / 2 + 9, height - 30);

        reconnectButton = this.addDrawableChild(
			new ButtonWidget.Builder(
				Text.of("Reconnect"),
                button -> this.getAutoReconnect().reconnect(parent)
            )
            .position(backButtonX, backButtonY + 24)
            .size(200, 20)
            .build()
        );

    }
}

