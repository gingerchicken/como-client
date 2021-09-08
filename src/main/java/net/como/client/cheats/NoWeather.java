package net.como.client.cheats;

import net.como.client.events.RenderWeatherEvent;
import net.como.client.events.TickRainSplashingEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class NoWeather extends Cheat {
    public NoWeather() {
        super("AntiBritish");

        this.description = "Hides the rain.";
    }

    @Override
    public void activate() {
        this.addListen(RenderWeatherEvent.class);
        this.addListen(TickRainSplashingEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWeatherEvent.class);
        this.addListen(TickRainSplashingEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWeatherEvent": {
                ((RenderWeatherEvent)event).ci.cancel();
                break;
            }
            case "TickRainSplashingEvent": {
                ((TickRainSplashingEvent)event).ci.cancel();
                break;
            }
        }
    }
}
