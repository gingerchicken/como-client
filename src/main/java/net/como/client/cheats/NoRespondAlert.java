package net.como.client.cheats;

import java.util.ArrayList;
import java.util.List;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.OnWorldTimeUpdateEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.Colour;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class NoRespondAlert extends Cheat {
    public NoRespondAlert() {
        super("NoRespondAlert");

        this.addSetting(new Setting("WarningTime", 1d));
        this.addSetting(new Setting("DisplayHeight", 150));
    }

    @Override
    public void activate() {
        this.addListen(OnWorldTimeUpdateEvent.class);
        this.addListen(SendPacketEvent.class);
        this.addListen(ClientTickEvent.class);
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnWorldTimeUpdateEvent.class);
        this.removeListen(ClientTickEvent.class);
        this.removeListen(InGameHudRenderEvent.class);
        this.removeListen(SendPacketEvent.class);
    }

    private double lastResp = -1;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                float noRespTime = (float) (CheatClient.getCurrentTime() - lastResp);
                if (noRespTime < (Double)this.getSetting("WarningTime").value) break;

                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                TextRenderer t = CheatClient.textRenderer;
                Text message = Text.of("Server not responded for ");
                Text timer = Text.of(String.format("%.2f", noRespTime));

                // Get the rendered width
                int width = t.getWidth(message) + t.getWidth(timer);
                
                // See where the user wants to put it on the screen
                int userHeight = (int)this.getSetting("DisplayHeight").value;

                int x = CheatClient.getClient().getWindow().getScaledWidth()/2 - width/2;
                int y = (int)((double)userHeight/CheatClient.getClient().getWindow().getScaleFactor());

                // Timer colour thing
                float badDistance = 15f - noRespTime;
                badDistance = badDistance < 0 ? 0 : badDistance;

                // Get the colour
                Colour timerColour = Colour.fromDistance(badDistance);

                // Render the text
                x = t.drawWithShadow(e.mStack, message, x, y, 0xFFFFFFFF);
                x = t.drawWithShadow(e.mStack, timer, x, y, RenderUtils.RGBA2Int((int)timerColour.r, (int)timerColour.g, (int)timerColour.b, 255));

                break;
            }
            case "OnWorldTimeUpdateEvent": {
                lastResp = CheatClient.getCurrentTime();

                break;
            }
        }
    }
}
