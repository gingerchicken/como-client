package net.como.client.modules.chat;

import java.util.HashMap;

import net.como.client.ComoClient;
import net.como.client.commands.CommandChatIgnore;
import net.como.client.events.AddMessageEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;

public class ChatIgnore extends Module {

    public ChatIgnore() {
        super("ChatIgnore");

        this.description = "A client-side ignore command";

        this.addSetting(new Setting("Phrases", new HashMap<String, Boolean>()));

        // Registering commandsCommandChatIgnore
        ComoClient.commandHandler.registerCommand(new CommandChatIgnore());

        this.setCategory("Chat");
    }

    // TODO potential bug with blocking our own messages.
    @SuppressWarnings("unchecked")
    private boolean shouldBlock(String msg) {
        HashMap<String, Boolean> phrases = (HashMap<String, Boolean>)this.getSetting("Phrases").value;

        for (String phrase : phrases.keySet()) {
            if (msg.equals(phrase) || msg.contains(phrase)) return true;
        }
        return false;
    }

    @Override
    public void activate() {
        this.addListen(AddMessageEvent.class);
        this.displayMessage("You can now add a phrase to emit from chat using the '.ignore' command!");
    }

    @Override
    public void deactivate() {
        this.removeListen(AddMessageEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "AddMessageEvent": {
                AddMessageEvent e = (AddMessageEvent)event;
                String rawMessage = e.chatText.getString();

                if (this.shouldBlock(rawMessage)) {
                    ComoClient.log(String.format("(BLOCKED MESSAGE) %s", rawMessage));

                    e.ci.cancel();
                }
            }
        }
    }
}