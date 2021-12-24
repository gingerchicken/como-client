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

    // TODO add more of these since they are super useful.
    public Boolean getBoolSetting(String name) {
        return (Boolean)this.getSetting(name).value;
    }

    public Integer getIntSetting(String name) {
        return (Integer)this.getSetting(name).value;
    }

    public Double getDoubleSetting(String name) {
        return (Double)this.getSetting(name).value;
    }

    public String getStringSetting(String name) {
        return (String)this.getSetting(name).value;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Boolean> getHashMapSetting(String name) {
        return (HashMap<String, Boolean>)(this.getSetting(name).value);
    }

    public Settings() { }
}
