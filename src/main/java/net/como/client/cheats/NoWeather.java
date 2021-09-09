package net.como.client.cheats;

import net.como.client.events.GetRainGradientEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class NoWeather extends Cheat {
    public NoWeather() {
        super("AntiBritish");

        this.description = "Hides the rain.";
    }

    @Override
    public void activate() {
        this.addListen(GetRainGradientEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetRainGradientEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetRainGradientEvent": {
                ((GetRainGradientEvent)event).cir.setReturnValue(0f);
                break;
            }
        }
    }
}
