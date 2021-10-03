package net.como.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.como.client.structures.Cheat;

public class Persistance {
    public static final String CONFIG_PATH = "como-config.json";
    Persistance() { }

    private static String readConfig(String path) throws FileNotFoundException {
        String json = "";
        
        // Read the file
        File file = new File(path);
        Scanner reader = new Scanner(file);

        while (reader.hasNext()) {
            json = json.concat(reader.next());
        }

        // Close the file
        reader.close();

        return json;
    }

    private static void writeConfig(String data, String path) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);

        writer.write(data);

        writer.close();
    }

    public static void loadConfig() {
        String data;
        try {
            data = readConfig(CONFIG_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("No config file found... creating one...");
            saveConfig();

            return;
        }

        // Load all of the flattened states
        HashMap<String, HashMap<String, String>> flat = new Gson().fromJson(data, new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType());
        for (String name : flat.keySet()) {
            Cheat cheat = CheatClient.Cheats.get(name);

            cheat.lift(flat.get(name));
        }
    }

    public static String makeConfig() {
        Gson gson = new Gson();

        // Save all of the cheats
        HashMap<String, HashMap<String, String>> flat = new HashMap<String, HashMap<String, String>>();
        for (String name : CheatClient.Cheats.keySet()) {
            Cheat cheat = CheatClient.Cheats.get(name);

            flat.put(name, cheat.flatten());
        }

        // TODO Save friends list

        return gson.toJson(flat);
    }

    public static void saveConfig() {
        String json = makeConfig();
        try {
            writeConfig(json, CONFIG_PATH);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
