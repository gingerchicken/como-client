package net.como.client.cheats;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import net.como.client.CheatClient;
import net.como.client.commands.BindsCommand;
import net.como.client.events.OnKeyEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ChatUtils;

public class Binds extends Cheat {
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
            return String.format("%s%s", CheatClient.commandHandler.delimiter, this.getCommand());
        }

        public Bind(int key, String command) {
            this.key = key;
            this.command = command;
        }

        public void executeCommand() {
            CheatClient.commandHandler.handle(this.getTriggerCommand());
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

        this.description = "Allows you to bind client commands to keys.";

        // TODO make this per bind
        this.addSetting(new Setting("HideCommandOutput", true));

        CheatClient.commandHandler.registerCommand(new BindsCommand());
    }

    @Override
    public void activate() {
        this.addListen(OnKeyEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnKeyEvent.class);
    }

    public boolean logNextKey = false;

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

                    logNextKey = false;
                    e.ci.cancel();

                    break;
                }

                if (CheatClient.getClient().currentScreen == null) this.fireBind(e.key);

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
}
