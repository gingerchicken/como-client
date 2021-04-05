package net.como.client.structures;

import java.util.HashMap;

public class Settings {
    private HashMap<String, Setting> settings = new HashMap<String, Setting>();

    public void addSetting(Setting setting) {
        settings.put(setting.name, setting);
    }

    public Setting getSetting(String name) {
        return this.settings.get(name);
    }

    public Settings() { }
}
