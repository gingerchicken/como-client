package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.DeathEvent;
import net.como.client.events.JumpEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class HomeGodMode extends Cheat {
    public HomeGodMode() {
        super("HomeGodMode");

        this.description = "Exploits the /sethome and /home feature on servers";
    }

    @Override
    public void activate() {
        this.addListen(DeathEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(DeathEvent.class);
    }

    private void setHome() {
        CheatClient.me().sendChatMessage("/sethome Death");
    }

    private void home() {
        CheatClient.me().sendChatMessage("/home Death");
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "DeathEvent": {
                this.setHome();
                CheatClient.me().requestRespawn();
                this.home();
                break;
            }
        }
    }
}
