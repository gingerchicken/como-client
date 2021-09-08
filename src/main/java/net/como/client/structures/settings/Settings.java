package net.como.client.structures.settings;

import java.util.HashMap;
import java.util.Set;

public class Settings {
    private HashMap<String, Setting> settings = new HashMap<String, Setting>();

    public void addSetting(Setting setting) {
        settings.put(setting.name, setting);
    }

    public Boolean settingExists(String name) {
        return this.settings.containsKey(name);
    }

    public Set<String> getSettings() {
        return this.settings.keySet();
    }

    public Setting getSetting(String name) {
        // Make sure that it actually exists.
        if (!this.settings.containsKey(name)) return null;

        return this.settings.get(name);
    }

    public Settings() { }
}
