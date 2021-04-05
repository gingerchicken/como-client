package net.como.client.structures;

public class Setting<T> {
    public final T defaultValue;
    public T value;

    public final String name;

    public Setting(String name, T defaultValue) {
        this.defaultValue = defaultValue;
        this.name = name;

        this.value = defaultValue;
    }
}
