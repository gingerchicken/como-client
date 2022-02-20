package net.como.client.structures.settings;

import java.util.ArrayList;
import java.util.List;

public class Setting {
    public final Object defaultValue;
    public Object value;

    public final String name;

    private List<String> suggestions = new ArrayList<>();

    public List<String> getSuggestions() {
        return this.suggestions;
    }

    public Setting(String name, Object defaultValue, String... valueSuggestions) {
        this(name, defaultValue);

        for (String sug : valueSuggestions) {
            this.suggestions.add(sug);
        }
    }

    public Setting(String name, Object defaultValue) {
        this.defaultValue = defaultValue;
        this.name = name;

        this.value = defaultValue;
        this.suggestions.add(defaultValue.toString());
    }
}
