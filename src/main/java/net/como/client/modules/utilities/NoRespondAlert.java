package net.como.client.modules.utilities;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.packet.OnWorldTimeUpdateEvent;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.events.render.InGameHudRenderEvent;
import net.como.client.misc.Colour;
import net.como.client.modules.Module;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class NoRespondAlert extends Module {
    public NoRespondAlert() {
        super("NoRespondAlert");

        this.addSetting(new Setting("WarningTime", 1d));
        this.addSetting(new Setting("DisplayHeight", 150));

        this.addSetting(new Setting("ShowWhenClosed", true));
    
        this.setDescription("Displays an alert when the server has stopped sending data.");
        
        this.setCategory("Utilities");
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
                if (!ComoClient.me().isAlive()) break;

                // Handle disconnection (i.e. when you block the ban packet)
                if (!this.getBoolSetting("ShowWhenClosed") && !ComoClient.me().networkHandler.getConnection().isOpen()) break;

                float noRespTime = (float) (ComoClient.getCurrentTime() - lastResp);
                if (noRespTime < (Double)this.getSetting("WarningTime").value) break;

                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                TextRenderer t = ComoClient.getInstance().textRenderer;
                Text message = Text.of("Server not responded for ");
                Text timer = Text.of(String.format("%.2f", noRespTime));

                // Get the rendered width
                int width = t.getWidth(message) + t.getWidth(timer);
                
                // See where the user wants to put it on the screen
                int userHeight = (int)this.getSetting("DisplayHeight").value;

                int x = ComoClient.getClient().getWindow().getScaledWidth()/2 - width/2;
                int y = (int)((double)userHeight/ComoClient.getClient().getWindow().getScaleFactor());

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
                lastResp = ComoClient.getCurrentTime();

                break;
            }
        }
    }
}
