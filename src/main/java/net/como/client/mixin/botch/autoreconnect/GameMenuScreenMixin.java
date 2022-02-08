package net.como.client.mixin.botch.autoreconnect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.modules.utilities.AutoReconnect;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }
    
    private ButtonWidget reconnectButton;

    // TODO wait this was in the last one, surely this should be a common function in an interface or something?
    private AutoReconnect getAutoReconnect() {
        return (AutoReconnect)ComoClient.Modules.get("autoreconnect");
    }

    private Boolean shouldDisplayButton() {
        return 
            this.getAutoReconnect().getBoolSetting("InGameButton") 
            && ServerUtils.getLastServer() != null
            && !ComoClient.getClient().isInSingleplayer()
            && !ComoClient.getClient().isConnectedToRealms();
    }

    private void reconnect() {
        MultiplayerScreen multiplayerScreen = new MultiplayerScreen(new TitleScreen());

        // Disconnect
        ClientUtils.disconnect(multiplayerScreen);
        
        // Reconnect
        this.getAutoReconnect().reconnect(multiplayerScreen);
    }

    @Inject(at = @At("TAIL"), method="init()V", cancellable = false)
    public void init(CallbackInfo ci) {
        if (!this.shouldDisplayButton()) return;

        int x = this.width / 2 - 102;
        int y = this.height / 4 + 120 - 16;

        this.reconnectButton = this.addDrawableChild(
			new ButtonWidget(x, y + 24, 204, 20,
				new LiteralText("Reconnect"),
                button -> this.reconnect()
            )
        );
    }
}
