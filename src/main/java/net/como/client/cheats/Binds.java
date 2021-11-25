package net.como.client.cheats;

import net.como.client.events.OnKeyEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class Binds extends Cheat {
    private enum KeyAction {
        DOWN,
        UP,
        HOLD;

        public static KeyAction fromInt(int x) {
            switch (x) {
                case 0:
                    return DOWN;
                case 1:
                    return UP;
                case 2:
                    return HOLD;
                default:
                    return null;
            }
        }
    }

    public Binds() {
        super("Binds");

        this.description = "Allows you to bind client commands to keys.";
    }

    @Override
    public void activate() {
        this.addListen(OnKeyEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnKeyEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnKeyEvent": {
                OnKeyEvent e = (OnKeyEvent)event;
                KeyAction action = KeyAction.fromInt(e.action);

                // For now, we will only handle key down.
                if (action != KeyAction.DOWN) break;

                break;
            }
        }
    }
}
