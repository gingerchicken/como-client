package net.como.client.items;

import java.io.InputStream;

import com.google.gson.Gson;

public class ComoItemFactory {
    private static final RawComoItem[] EMPTY = new RawComoItem[0];

    /**
     * Gets the raw como items from the given input stream.
     * @param stream The input stream for a JSON file.
     * @return The raw como items.
     */
    public static RawComoItem[] fromJsonStream(InputStream stream) {
        RawComoItem[] items;

        String itemData = "";

        // Attempt to read the stream
        try {
            itemData = new String(stream.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY;
        }

        // Attempt to parse the data
        items = new Gson().fromJson(itemData, RawComoItem[].class);

        // Return the items
        return items;
    }
}
