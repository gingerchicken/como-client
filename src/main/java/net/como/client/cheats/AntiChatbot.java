package net.como.client.cheats;

import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.como.client.structures.DefaultMCMessage;
import net.minecraft.text.Text;

public class AntiChatbot extends Cheat {

    public AntiChatbot() {
        super("AntiChatbot");

        this.description = "Hide potential chat spammers and advertisement messages.";
    }

    // I tried to think of the rubbish that is spammed in 2b2t chat
    private int minLength = 3;

    // Potential Future settings
    // TODO make these settings
    private String[] bannedKeywords = new String[]{"discord", ".xyz", ".org", ".com", "shop", "kits", "free"};
    private boolean blockUpperCase = true;
    private float maxUpperPercentage = 0.5f;

    private HashMap<String, HashMap<String, Integer>> playerMessageFreq = new HashMap<String, HashMap<String, Integer>>();
    private HashMap<String, Integer> playerMessageTotals = new HashMap<String, Integer>();

    private boolean isBot(String message) {
        DefaultMCMessage mcMessage = new DefaultMCMessage(message);

        if (mcMessage.isDefaultMCMessage()) {
            // Get the total number of messages and add one to the value
            Integer totalMessages = playerMessageTotals.get(mcMessage.getUsername());
            totalMessages = (totalMessages == null ? 0 : totalMessages) + 1;
            playerMessageTotals.put(mcMessage.getUsername(), totalMessages);

            if (totalMessages > 5) {
                HashMap<String, Integer> freqs = playerMessageFreq.get(mcMessage.getUsername());
                freqs = freqs == null ? new HashMap<String, Integer>() : freqs;
            
                // Get the total number of said message with the total
                Integer totalPosts = freqs.get(mcMessage.getMessage());
                totalPosts = (totalPosts == null ? 0 : totalPosts) + 1; 
                freqs.put(mcMessage.getMessage(), totalPosts);
    
                playerMessageFreq.put(mcMessage.getUsername(), freqs);

                Integer totalUniqueClasses = freqs.size();
    
                // TODO maybe mark the account as a bot?
                float probOfNewMessage = (float)totalUniqueClasses/(float)totalMessages;
                if (probOfNewMessage < 0.25f) return true;
            }
        }

        // Used to check word lengths
        String[] words = message.split(" ");

        // Block all caps
        if (this.blockUpperCase) {
            int totalUpper = 0;
            for (String word : words) {
                if (word == word.toUpperCase()) totalUpper++;
            }

            float percUpper = (float)totalUpper/(float)words.length;
            if (percUpper >= this.maxUpperPercentage) {
                return true;
            }
            
        }

        if (words.length < this.minLength) return false;

        message = message.toLowerCase();

        for (String badWord : bannedKeywords) {
            if (message.contains(badWord)) return true;
        }

        return false;
    }

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onAddMessage": {
                if (!this.isEnabled()) break;

                Text message = (Text)args[0];
                // int chatId = (int)args[1];
                CallbackInfo ci = (CallbackInfo)args[2];

                String rawMessage = message.getString();

                if (this.isBot(rawMessage)) {
                    System.out.println("(BLOCKED MESSAGE) " + rawMessage);

                    ci.cancel();
                }
            }
        }
    }
}