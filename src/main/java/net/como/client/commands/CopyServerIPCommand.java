package net.como.client.commands;

import net.como.client.commands.structures.Command;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.ServerUtils;
import net.minecraft.client.network.ServerInfo;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class CopyServerIPCommand extends Command {
    public CopyServerIPCommand() {
        super("copyip", "<no args>", "copies the last server's address to the clipboard");
    }

    @Override
    public Boolean trigger(String[] args) {
        ServerInfo lastServer = ServerUtils.getLastServer();

        if (lastServer == null) {
            this.displayChatMessage(
                String.format("%sUnable to find server.", ChatUtils.RED)
            );

            return true;
        }

        // Get server address and print it
        String ip = lastServer.address;
        this.displayChatMessage(String.format("Copied server address to clipboard: %s%s", ChatUtils.GREEN, ip));

        // TODO restore this
        System.setProperty("java.awt.headless", "false");

        // Add to clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(ip), null);

        return true;
    }
}
