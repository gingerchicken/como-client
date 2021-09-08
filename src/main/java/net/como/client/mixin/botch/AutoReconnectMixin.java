package net.como.client.mixin.botch;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.CheatClient;
import net.como.client.cheats.AutoReconnect;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(DisconnectedScreen.class)
public class AutoReconnectMixin extends Screen {
    private AutoReconnect _autoReconnect;
    private ButtonWidget reconnectButton;

    private AutoReconnect getAutoReconnect() {
        // Make sure that it isn't null
        if (this._autoReconnect == null) {
            this._autoReconnect = (AutoReconnect)CheatClient.Cheats.get("autoreconnect");
        } 

        // Find it
        return this._autoReconnect;
    }

    // Get private variables
    @Shadow
	@Final
	private Screen parent;
	
	@Shadow
	private int reasonHeight;

    public AutoReconnectMixin(Text title) {
        super(title);
    }

    @Override
    public void tick() {
        // Make sure that we are should display
        if (!this.getAutoReconnect().isEnabled()) return;

        // Make sure AutoReconnect is enabled and not manual reconnect
        if (this.isManualMode()) return;

        reconnectButton.setMessage(
            new LiteralText(String.format("AutoReconnect (%f)", this.getAutoReconnect().workCountdown(this.parent)))
        );
    }

    private boolean isManualMode() {
        return (boolean)this.getAutoReconnect().getSetting("Manual").value;
    }

    @Inject(at = @At("TAIL"), method="init()V", cancellable = false) 
    public void init(CallbackInfo ci) {
        if (!this.getAutoReconnect().isEnabled()) return;

        int backButtonX = width / 2 - 100;
		int backButtonY = Math.min(height / 2 + this.reasonHeight / 2 + 9, height - 30);

        reconnectButton = this.addDrawableChild(
			new ButtonWidget(backButtonX, backButtonY + 24, 200, 20,
				new LiteralText("Reconnect"),
                button -> this.getAutoReconnect().reconnect(parent)
            )
        );

    }
}

