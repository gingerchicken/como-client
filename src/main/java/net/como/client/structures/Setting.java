package net.como.client.structures;

public class Setting {
    public final Object defaultValue;
    public Object value;

    public final String name;

    public Setting(String name, Object defaultValue) {
        this.defaultValue = defaultValue;
        this.name = name;

        this.value = defaultValue;
    }
}
