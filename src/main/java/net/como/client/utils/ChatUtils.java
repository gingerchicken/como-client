package net.como.client.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.include.com.google.common.base.Strings;

import net.como.client.ComoClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

public class ChatUtils {
    public static boolean hideNextChat = false;

    public final static String BLACK        = "\u00a70";
    public final static String DARKBLUE     = "\u00a71";
    public final static String DARKGREEN    = "\u00a72";
    public final static String DARKAQUA     = "\u00a73";;
    public final static String DARKRED      = "\u00a74";
    public final static String DARKPURPLE   = "\u00a75";
    public final static String GOLD         = "\u00a76";
    public final static String GRAY         = "\u00a77";
    public final static String DARKGRAY     = "\u00a78";
    public final static String BLUE         = "\u00a79";
    public final static String GREEN        = "\u00a7a";
    public final static String AQUA         = "\u00a7b";
    public final static String RED          = "\u00a7c";
    public final static String LIGHTPURPLE  = "\u00a7d";
    public final static String YELLOW       = "\u00a7e";
    public final static String WHITE        = "\u00a7f";
    
    public static void displayMessage(String message) {
        if (hideNextChat) {
            hideNextChat = false;

            return;
        }

        // Make sure that they are in game.
        if (!ClientUtils.inGame()) return;

        MinecraftClient client = ComoClient.getClient();
        ChatHud chatHud = client.inGameHud.getChatHud();

        chatHud.addMessage(Text.of(message));
    }

    public static String chatPrefix(String name) {
        return String.format("%s[%s%s%s] ", ChatUtils.WHITE, ChatUtils.GREEN, name, ChatUtils.WHITE);
    }

    static Random random = new Random();
    public static String randomCase(String str) {
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (random.nextInt() % 2 == 0) {
                chars[i] = Character.toUpperCase(chars[i]);
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }

        return new String(chars);
    }

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
    public static Integer getStartOfCurrentWord(String input) {
        int i = 0;
        
        if (Strings.isNullOrEmpty(input)) return i;
        
        Matcher matcher = WHITESPACE_PATTERN.matcher(input);
        
        while (matcher.find()) {
            i = matcher.end();
        }

        return i;
    }

    public static void sendChatMessage(String message, boolean handleCommands) {
        // If it starts with a / send a command
        if (message.startsWith("/") && handleCommands) {
            ComoClient.getClient().player.sendCommand(message.substring(1), null);
            return;
        }
        
        ComoClient.me().sendChatMessage(message, null);
    }

    public static void sendChatMessage(String message) {
        sendChatMessage(message, true);
    }
}
