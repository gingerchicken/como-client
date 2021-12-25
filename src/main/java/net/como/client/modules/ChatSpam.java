package net.como.client.modules;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import joptsimple.internal.Strings;
import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;

public class ChatSpam extends Module {

    public ChatSpam() {
        super("ChatSpam");

        this.description = "Spams the chat with different messages";

        this.addSetting(new Setting("Delay", 0.5d));

        this.addSetting(new Setting("Messages", new HashMap<String, Boolean>()));

        this.addSetting(new Setting("RandPrefix", true));
        this.addSetting(new Setting("HashPrefix", true));

        this.addSetting(new Setting("RandomCase", false));
    }

    private Random random = new Random();

    // Returns a random message for the chat.
    private String getMessage() {
        Set<String> messages = this.getHashMapSetting("Messages").keySet();
        if (messages.isEmpty()) return "Como Client on top!";
    
        // Get the target index.
        int i = this.random.nextInt() % messages.size();
        String msg = Strings.EMPTY;
        
        // Select the message from the "linked list"
        int j = 0;
        for (String message : messages) {
            msg = message;

            if (j == i) break;
            j++;
        }

        return msg;
    }

    private String randomCase(String str) {
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (this.random.nextInt() % 2 == 0) {
                chars[i] = Character.toUpperCase(chars[i]);
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }

        return new String(chars);
    }

    private String getRandomPrefix(Boolean hash) {
        String prefix = String.format("%f", ComoClient.getCurrentTime());
        if (!hash) return prefix;

        try {
            String hashed = Strings.EMPTY;
            byte[] hashData = MessageDigest.getInstance("MD5").digest(prefix.getBytes());

            for (byte b : hashData) {
                hashed = hashed.concat(String.format("%02x", b));
            }

            prefix = hashed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return prefix;
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private Double lastPostTime = 0d;

    private String generateMessage() {
        String msg = this.getMessage();

        msg = this.getBoolSetting("RandPrefix") ? String.format("%s [%s]", msg, this.getRandomPrefix(this.getBoolSetting("HashPrefix"))) : msg;
        msg = this.getBoolSetting("RandomCase") ? this.randomCase(msg) : msg;

        return msg;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                Double delay = this.getDoubleSetting("Delay");
                Double deltaTime = ComoClient.getCurrentTime() - this.lastPostTime;

                // Check if we should post again
                if (delay > deltaTime) break;

                // Send a message
                ComoClient.me().sendChatMessage(this.generateMessage());

                // Update last post time
                this.lastPostTime = ComoClient.getCurrentTime();

                break;
            }
        }
    }
}
