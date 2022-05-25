package net.como.client.modules.utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import net.como.client.ComoClient;
import net.como.client.GeneralConfig;
import net.como.client.commands.structures.Command;
import net.como.client.events.Event;
import net.como.client.events.io.OnKeyEvent;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
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

    public static class Bind {
        private int key;
        private String command;

        public int getKey() {
            return this.key;
        }

        public String getCommand() {
            return this.command;
        }

        private String getTriggerCommand() {
            return String.format("%s%s", ComoClient.getInstance().commandHandler.delimiter, this.getCommand());
        }

        public Bind(int key, String command) {
            this.key = key;
            this.command = command;
        }

        public void executeCommand() {
            ComoClient.getInstance().commandHandler.handle(this.getTriggerCommand());
        }
    }

    private HashMap<Integer, List<Bind>> binds = new HashMap<Integer, List<Bind>>();

    public boolean addBind(Bind bind) {
        if (!binds.containsKey(bind.getKey())) {
            binds.put(bind.getKey(), new ArrayList<Bind>());
        }

        // Make sure it ain't a dupe.
        if (this.getBindIndex(bind) != -1) return false;

        // TODO check if the key is valid.
        binds.get(bind.getKey()).add(bind);

        return true;
    }
    public boolean addBind(int key, String command) {
        return this.addBind(new Bind(key, command));
    }
    
    public boolean removeBind(int key, String command) {
        int index = getBindIndex(key, command);

        // Make sure that the item exists.
        if (index == -1) return false;

        // Remove the item from the list
        binds.get(key).remove(index);

        // If its empty we can just remove the entire thing.
        if (binds.get(key).size() == 0) return this.removeBind(key);

        // All done!
        return true;
    }
    public boolean removeBind(int key) {
        // Check that we actually have the key bound.
        if (!binds.containsKey(key)) return false;

        // Remove the entire object.
        binds.remove(key);

        // We good.
        return true;
    }
    
    public boolean fireBind(int key) {
        if (!this.binds.containsKey(key)) return false;

        List<Bind> active = this.binds.get(key);

        for (Bind bind : active) {
            ChatUtils.hideNextChat = this.getBoolSetting("HideCommandOutput");
            bind.executeCommand();
        }

        return true;
    }

    private int getBindIndex(int key, String command) {
        return this.getBindIndex(new Bind(key, command));
    }
    private int getBindIndex(Bind bind) {
        int i = -1;
        if (!binds.containsKey(bind.key)) {
            return i;
        }

        List<Bind> active = binds.get(bind.key);
        
        int j = 0;
        for (Bind b : active) {
            if (b.getCommand().equals(bind.command)) {
                i = j;
                break;
            }

            j++;
        }

        return i;
    }

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
                if (ComoClient.getClient().currentScreen == null) this.fireBind(e.key);

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

    // This works but it is nasty
    @Override
    public void lift(HashMap<String, String> data) {
        if (data.containsKey("binds")) {
            Gson gson = new Gson();

            String rawBindsObj = data.get("binds");

            HashMap<String, String> bindsObj = new HashMap<>();
            bindsObj = gson.fromJson(rawBindsObj, bindsObj.getClass());

            for (String key : bindsObj.keySet()) {
                Type type = new TypeToken<ArrayList<Bind>>() {}.getType();
                List<Bind> bindsList = gson.fromJson(bindsObj.get(key), type);

                this.binds.put(Integer.parseInt(key), bindsList);
            }
    
            data.remove("binds");
        }

        super.lift(data);
    }

    @Override
    public HashMap<String, String> flatten() {
        HashMap<String, String> data = super.flatten();
        
        Gson gson = new Gson();
        HashMap<String, String> flatBindsList = new HashMap<>();
        for (Integer key : this.binds.keySet()) {
            List<Bind> flatBinds = new ArrayList<>();

            for (Bind bind : this.binds.get(key)) {
                flatBinds.add(bind);
            }

            flatBindsList.put(key.toString(), gson.toJson(flatBinds));
        }

        data.put("binds", gson.toJson(flatBindsList));

        return data;
    }

    @Override
    public Iterable<Command> getCommands() {
        List<Command> commands = new ArrayList<>();

        commands.add(new AddBind());
        commands.add(new RemoveBind());
        commands.add(new LogNext());

        return commands;
    }

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

            if (!this.getBinds().addBind(key, command)) {
                this.getBinds().displayMessage(String.format("%sFailed to add key bind.", ChatUtils.RED));
                return true;
            }

            this.getBinds().displayMessage(String.format("Bound %s to run \"%s\".", this.keyBindString(key), command));
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
                    return true;
                }

                this.getBinds().displayMessage(String.format("Removed key bind '%s'.", this.keyBindString(key)));
                return true;
            }

            String command = this.getCommandArg(args);

            // Handle key + command
            if (!this.getBinds().removeBind(key, command)) {
                this.getBinds().displayMessage(String.format("%sFailed to remove key bind for key '%s' with command '%s'.", ChatUtils.RED, this.keyBindString(key), command));
                return true;
            }

            this.getBinds().displayMessage(String.format("Removed bind '%s' from key '%s'.", command, this.keyBindString(key)));

            return true;

        }
    }
}
