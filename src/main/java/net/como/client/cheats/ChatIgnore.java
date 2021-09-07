package net.como.client.cheats;

import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.commands.CommandChatIgnore;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;

import net.minecraft.text.Text;

public class ChatIgnore extends Cheat {

    public ChatIgnore() {
        super("ChatIgnore");

        this.description = "A client-side ignore command";

        this.addSetting(new Setting("Phrases", new HashMap<String, Boolean>()));

        // Registering commandsCommandChatIgnore
        CheatClient.commandHandler.registerCommand(new CommandChatIgnore());
    }

    // TODO potential bug with blocking our own messages.
    private boolean shouldBlock(String msg) {
        HashMap<String, Boolean> phrases = (HashMap<String, Boolean>)this.getSetting("Phrases").value;

        for (String phrase : phrases.keySet()) {
            if (msg.equals(phrase) || msg.contains(phrase)) return true;
        }
        return false;
    }

    public void activate() {
        this.displayMessage("You can now add a phrase to emit from chat using the '.ignore' command!");
    }

    @Override
    public void receiveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onAddMessage": {
                if (!this.isEnabled()) break;

                Text message = (Text)args[0];
                CallbackInfo ci = (CallbackInfo)args[2];

                String rawMessage = message.getString();

                if (this.shouldBlock(rawMessage)) {
                    System.out.println("(BLOCKED MESSAGE) " + rawMessage);

                    ci.cancel();
                }
            }
        }
    }
}