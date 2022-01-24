package net.como.client.modules.packet;

import net.como.client.events.GetClientModNameEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;

public class FakeClient extends Module {
    public FakeClient() {
        super("FakeClient", true);

        this.description = "Makes the client appear as vanilla to any servers";
        this.setCategory("Packet");

        this.addSetting(new Setting("Client", "vanilla"));
    }

    @Override
    public String listOption() {
        return this.getStringSetting("Client");
    }

    @Override
    public void activate() {
        this.addListen(GetClientModNameEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetClientModNameEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetClientModNameEvent": {
                GetClientModNameEvent e = (GetClientModNameEvent)event;

                e.cir.setReturnValue(this.getStringSetting("Client"));

                break;
            }
        }
    }
}
