package net.como.client.commands;

import java.util.Arrays;

import net.como.client.CheatClient;
import net.como.client.cheats.Binds;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
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
            return (Binds)CheatClient.Cheats.get("binds");
        }

        public String getCommandArg(String[] args) {
            return String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        public Integer getKeyArg(String[] args) {
            try {
                return Integer.valueOf(args[0]);
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
            super("add", "bind add <key code> <command (without dot e.g. \"flight\")>", "Make a new key binding");
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

            this.getBinds().displayMessage(String.format("Bound %d to run \"%s\".", key, command));
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
            super("remove", "<key id> <optional specific command>", "Removes a bind from the list of binds");
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
                    this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%d'.", ChatUtils.RED, key));
                }
                return true;
            }

            String command = this.getCommandArg(args);

            // Handle key + command
            if (!this.getBinds().removeBind(key, command)) {
                this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%d' with command '%s'.", ChatUtils.RED, key, command));
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
