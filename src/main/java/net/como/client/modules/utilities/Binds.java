package net.como.client.modules.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import com.google.gson.Gson;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.components.systems.binds.Bind;
import net.como.client.components.systems.binds.BindsSystem;
import net.como.client.components.systems.binds.impl.CommandBind;
import net.como.client.components.systems.binds.impl.ModuleBind;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.io.OnKeyEvent;
import net.como.client.modules.Module;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.ClientUtils;

public class Binds extends Module {
    private static enum KeyAction {
        DOWN,
        UP,
        HOLD;

        public static KeyAction fromInt(int x) {
            switch (x) {
                case 0:
                    return UP;
                case 1:
                    return DOWN;
                case 2:
                    return HOLD;
                default:
                    return null;
            }
        }
    }

    /**
     * Used for any binds etc on the client
     */
    protected BindsSystem binds = new BindsSystem();

    public Binds() {
        super("Binds", true);

        this.setDescription("Allows you to bind client commands to keys.");

        // TODO make this per bind
        this.addSetting(new Setting("HideCommandOutput", true));
        this.addSetting(new Setting("GUIKey", true));

        this.setCategory("Utilities");
    }

    @Override
    public void activate() {
        this.addListen(OnKeyEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnKeyEvent.class);
    }

    private Boolean isChatDelimiter(Integer key) {
        char keyChar = 0;
        try {
            keyChar = Character.toChars(key)[0];
        } catch (Exception e) {

        }

        return (keyChar == ComoClient.getInstance().commandHandler.delimiter.charAt(0));
    }

    public boolean logNextKey = false;

    public Integer lastLogKey = null;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnKeyEvent": {
                OnKeyEvent e = (OnKeyEvent)event;
                KeyAction action = KeyAction.fromInt(e.action);

                // For now, we will only handle key down.
                if (action != KeyAction.DOWN) break;

                // Print the key that the user pressed if they want us to do that.
                if (logNextKey) {
                    this.displayMessage(String.format("The key you just pressed had code %s%d", ChatUtils.GREEN, e.key));
                    lastLogKey = e.key;

                    logNextKey = false;
                    e.ci.cancel();

                    break;
                }

                // Handle the keypress
                if (ComoClient.getClient().currentScreen == null) binds.fireBinds(e.key);

                // Open chat if it is our command button.
                if (this.isChatDelimiter(e.key) && ComoClient.getClient().currentScreen == null) {
                    ClientUtils.openChatScreen();
                }

                // Open ClickGUI (if we dont have meteor)
                if (!ComoClient.isMeteorLoaded() && this.getBoolSetting("GUIKey") && e.key == ComoClient.getInstance().config.menuKey) {
                    ChatUtils.hideNextChat = true;
                    ComoClient.getInstance().getModules().get("clickgui").toggle();
                }

                break;
            }
        }
    }

    // TODO replace the lift/flatten system, it is disgusting.

    @Override
    public HashMap<String, String> flatten() {
        HashMap<String, String> data = super.flatten();
        
        Gson gson = new Gson();
        HashMap<String, String> flatBindsList = new HashMap<>();
        for (int key : this.binds) {
            List<String> flatBinds = new ArrayList<>();

            for (Bind bind : this.binds.getKeyBinds(key)) {
                String value = "";

                if (bind instanceof CommandBind) {
                    CommandBind commandBind = (CommandBind)bind;

                    value = commandBind.getCommand();
                } else if (bind instanceof ModuleBind) {
                    ModuleBind commandBind = (ModuleBind)bind;

                    value = commandBind.getModule().getName().toLowerCase();
                }

                flatBinds.add(value);
            }

            flatBindsList.put(String.valueOf(key), gson.toJson(flatBinds));
        }

        data.put("binds", gson.toJson(flatBindsList));

        return data;
    }

    // This works but it is nasty
    @Override
    public void lift(HashMap<String, String> data) {
        if (data.containsKey("binds")) {
            Gson gson = new Gson();

            String rawBindsObj = data.get("binds");

            HashMap<String, String> bindsObj = new HashMap<>();
            bindsObj = gson.fromJson(rawBindsObj, bindsObj.getClass());

            for (String key : bindsObj.keySet()) {
                int keyCode = Integer.parseInt(key);
                
                // Retrieve the array of the commands
                String[] commands = gson.fromJson(bindsObj.get(key), String[].class);

                for (String command : commands) {
                    if (command.equals("")) continue;

                    // If the command is a module, then we need to find it and bind it.
                    if (ComoClient.getInstance().getModules().containsKey(command)) {
                        Module module = ComoClient.getInstance().getModules().get(command);

                        this.binds.addBind(keyCode, new ModuleBind(module));
                    } else {
                        this.binds.addBind(keyCode, new CommandBind(command));
                    }
                }
            }
    
            data.remove("binds");
        }

        super.lift(data);
    }

    @Override
    public Iterable<Command> getCommands() {
        List<Command> commands = new ArrayList<>();

        commands.add(new AddBind());
        commands.add(new RemoveBind());
        commands.add(new LogNext());
        commands.add(new ListBind());

        return commands;
    }

    private static class BindsSub extends Command {
        public BindsSub(String command, String helpText, String description) {
            super(command, helpText, description);
        }

        protected boolean isModuleCommand(String command) {
            return this.getModule(command) != null;
        }

        protected Module getModule(String command) {
            ComoClient cc = ComoClient.getInstance();

            // Strip it if it begins with the prefix
            if (command.startsWith(cc.config.commandPrefix)) {
                command = command.substring(cc.config.commandPrefix.length());
            }

            // Check if we have the module with this name
            if (!cc.getModules().containsKey(command)) return null;

            // If it exists then return it
            return cc.getModules().get(command);
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            if (args.length < 2) return true;

            return false;
        }

        public Binds getBinds() {
            return (Binds)ComoClient.getInstance().getModules().get("binds");
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

            boolean isModule = this.isModuleCommand(command);
            Bind bind = isModule ? new ModuleBind(this.getModule(command)) : new CommandBind(command);

            // Add the bind
            this.getBinds().binds.addBind(key, bind);
            this.getBinds().displayMessage(String.format("Bound %s to run %s \"%s\".", this.keyBindString(key), isModule ? "module" : "command", command));

            return true;
        }

        @Override
        public List<String> getSuggestions() {
            List<String> sugs = new ArrayList<>();

            if (this.getBinds().lastLogKey != null) sugs.add(this.getBinds().lastLogKey.toString());

            // All letters
            for (int i = (int)'a'; i <= (int)'z'; i++) {
                Character c = (char)i;
                sugs.add(c.toString());
            }

            return sugs;
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

    private static class ListBind extends BindsSub {
        @Override
        public boolean shouldShowHelp(String[] args) {
            return (args.length == 0);
        }
        
        public ListBind() {
            super("list", "binds list <key to check>", "Lists all the binds on a key");
        }

        @Override
        public Boolean trigger(String[] args) {
            if (this.handleHelp(args)) return true;

            int key = this.getKeyArg(args);
            if (checkKey(key)) return true;

            // Get the binds for this key
            Queue<Bind> binds = this.getBinds().binds.getKeyBinds(key);

            if (binds == null) {
                this.getBinds().displayMessage(String.format("%sNo binds on key %s.", ChatUtils.RED, this.keyBindString(key)));

                return true;
            }

            this.getBinds().displayMessage(String.format("Binds queue for key %s:", this.keyBindString(key)));

            int i = 1;
            for (Bind bind : binds) {
                this.getBinds().displayMessage(String.format("%d.  %s", i, bind.toString()));
                i++;
            }

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
                if (!this.getBinds().binds.removeBind(key)) {
                    this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%s'.", ChatUtils.RED, this.keyBindString(key)));
                    return true;
                }

                this.getBinds().displayMessage(String.format("Removed key bind '%s'.", this.keyBindString(key)));
                return true;
            }

            String command = this.getCommandArg(args);
            boolean success = false;

            // Find the bind with the specific command
            Queue<Bind> binds = this.getBinds().binds.getKeyBinds(key);

            // Create a new predicate

            Predicate<Bind> check = this.isModuleCommand(command) ?
                    // If it is a module command then check if the module is the same
                    (Bind bind) -> bind instanceof ModuleBind && ((ModuleBind)bind).getModule().getName().toLowerCase().equals(command) :

                    // If it is a command then check if the command is the same
                    (Bind bind) -> bind instanceof CommandBind && ((CommandBind)bind).getCommand().equals(command);
                

            for (Bind bind : binds) {
                if (!check.test(bind)) continue;

                success = true;
                this.getBinds().binds.removeBind(key, bind);
            }

            // Handle key + command
            if (!success) {
                this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%s' with command '%s'.", ChatUtils.RED, this.keyBindString(key), command));
                return true;
            }

            this.getBinds().displayMessage(String.format("Removed bind '%s' from key '%s'.", command, this.keyBindString(key)));

            return true;

        }
    }
}
