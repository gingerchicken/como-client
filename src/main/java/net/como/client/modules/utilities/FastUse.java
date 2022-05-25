package net.como.client.modules.utilities;

import net.como.client.ComoClient;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.interfaces.mixin.IClient;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;
import net.como.client.misc.settings.Setting;
import net.minecraft.client.MinecraftClient;

public class FastUse extends Module {

    public FastUse() {
        super("FastUse");

        this.setCategory("Packet");

        this.addSetting(new Setting("TickDelay", 0));

        this.setDescription("Allows you to use items at light speed.");
    }
    
    @Override
    public void activate() {
        this.resetDelay();
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private Integer delayTick = 0;
    private void resetDelay() {
        this.delayTick = 0;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                MinecraftClient client = ComoClient.getClient(); 

                if (!client.options.useKey.isPressed()) {
                    this.resetDelay();
                    break;
                }

                // Ignore if we are still on a delay
                if (this.delayTick > 0) {
                    this.delayTick--;
                    break;
                }

                IClient clientAccessor = (IClient)client;
                clientAccessor.performItemUse();
                this.delayTick = this.getIntSetting("TickDelay");

                break;
            }
        }
    }
}
