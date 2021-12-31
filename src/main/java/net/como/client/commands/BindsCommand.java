package net.como.client.commands;

import java.util.Arrays;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.modules.utilities.Binds;
import net.como.client.utils.ChatUtils;

public class BindsCommand extends CommandNode {
    private static class BindsSub extends Command {
        public BindsSub(String command, String helpText, String description) {
            super(command, helpText, description);
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            if (args.length < 2) return true;

            return false;
        }

        public Binds getBinds() {
            return (Binds)ComoClient.Modules.get("binds");
        }

        public boolean shouldDisplayKey(Integer keyCode) {
            char c = this.fromKeyCode(keyCode);

            return (Character.isLetterOrDigit(c));
        }

        public char fromKeyCode(Integer keyCode) {
            return Character.toUpperCase((char)(int)keyCode);
        }

        public String keyBindString(Integer keyCode) {
            return this.shouldDisplayKey(keyCode) ? String.format("%c", this.fromKeyCode(keyCode)) : String.format("0x%X", keyCode & 0xFFFFF);
        }

        public String getCommandArg(String[] args) {
            return String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        public Integer getKeyArg(String[] args) {
            String arg = args[0];

            // What if they gave a letter
            if (arg.length() == 1) {
                char c = arg.charAt(0);

                // Make sure that it is a letter
                if (Character.isLetter(c)) {
                    // Get the uppercase since this is what is going to be caught by minecraft key.
                    c = Character.toUpperCase(c);

                    return (int)(c);
                }
            }

            try {
                return Integer.valueOf(arg);
            } catch (Exception e) {
                return -1;
            }
        }

        public Boolean checkKey(int key) {
            if (key == -1) {
                this.getBinds().displayMessage(String.format("%sInvalid key, make sure it is the right key.", ChatUtils.RED));

                return true;
            }

            return false;
        }
    }

    private static class AddBind extends BindsSub {
        public AddBind() {
            super("add", "bind add <key code|character> <command (without dot e.g. \"flight\")>", "Make a new key binding");
        }

        @Override
        public Boolean trigger(String[] args) {
            if (this.handleHelp(args)) return true;

            int key = this.getKeyArg(args);
            String command = this.getCommandArg(args);

            if (checkKey(key)) return true;

            if (!this.getBinds().addBind(key, command)) {
                this.getBinds().displayMessage(String.format("%sFailed to add key bind.", ChatUtils.RED));
                return true;
            }

            this.getBinds().displayMessage(String.format("Bound %s to run \"%s\".", this.keyBindString(key), command));
            return true;
        }
    }

    private static class LogNext extends BindsSub {
        public LogNext() {
            super("lognext", "", "Prints the next key's code in chat.");
        }

        @Override
        public Boolean trigger(String[] args) {
            this.getBinds().logNextKey = true;

            this.getBinds().displayMessage("Press any key...");

            return true;
        }
    }

    // TODO add a list binds command

    private static class RemoveBind extends BindsSub {
        public RemoveBind() {
            super("remove", "<key id|character> <optional specific command>", "Removes a bind from the list of binds");
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            return (args.length == 0);
        }

        @Override
        public Boolean trigger(String[] args) {
            if (this.handleHelp(args)) return true;

            int key = this.getKeyArg(args);

            if (checkKey(key)) return true;

            // Handle entire key
            if (args.length == 1) {
                if (!this.getBinds().removeBind(key)) {
                    this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%s'.", ChatUtils.RED, this.keyBindString(key)));
                }
                return true;
            }

            String command = this.getCommandArg(args);

            // Handle key + command
            if (!this.getBinds().removeBind(key, command)) {
                this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%s' with command '%s'.", ChatUtils.RED, this.keyBindString(key), command));
            }
            return true;

        }
    }

    public BindsCommand() {
        super("bind", "Allows you to manager your Como Client key binds.");

        this.addSubCommand(new AddBind());
        this.addSubCommand(new RemoveBind());
        this.addSubCommand(new LogNext());
    }
    
}
