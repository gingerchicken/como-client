package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.HandleDisconnectionEvent;
import net.como.client.events.OnDisconnectedEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.ClientUtils;
import net.minecraft.text.Text;

public class AntiKick extends Module {
    public AntiKick() {
        super("AntiKick");

        this.description = "Blocks packets that cause you to disconnect from the server";

        this.setCategory("Packet");
    }

    @Override
    public String listOption() {
        return this.wasKicked ? "Disconnected" : "Connected";
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(OnDisconnectedEvent.class);
        this.addListen(HandleDisconnectionEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnDisconnectedEvent.class);
        this.removeListen(HandleDisconnectionEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    private void displayReason(String reason) {
        if (reason == null || reason.isBlank()) {
            this.displayMessage("You were disconnected from the server!");
            return;
        }

        this.displayMessage(String.format("You were disconnected from the server: %s%s", ChatUtils.RED, reason));
    }

    private void displayReason(Text text) {
        this.displayReason(ClientUtils.getTextString(text));
    }

    private Boolean wasKicked = false;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (ComoClient.me().networkHandler.getConnection().isOpen() && this.wasKicked) {
                    this.wasKicked = false;
                }
                
                break;
            }

            case "OnDisconnectedEvent": {
                OnDisconnectedEvent e = (OnDisconnectedEvent)event;

                // Stop the disconnect.
                e.ci.cancel();

                // Display reason
                this.displayReason(e.reason);

                this.wasKicked = true;
                
                break;
            }

            case "HandleDisconnectionEvent": {
                HandleDisconnectionEvent e = (HandleDisconnectionEvent)event;

                if (ComoClient.me() == null) break;

                // Prevent it (this will stop the handleDisconnect "being called twice" message)
                if (this.wasKicked) e.ci.cancel();
                
                break;
            }
        }
    }
}
