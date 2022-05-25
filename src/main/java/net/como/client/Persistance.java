package net.como.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.como.client.misc.Module;

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

    public static boolean loadConfig() {
        String data;
        try {
            data = readConfig(CONFIG_PATH);
        } catch (FileNotFoundException e) {
            ComoClient.log("No config file found... creating one...");
            saveConfig();

            return false;
        }

        // Load all of the flattened states
        HashMap<String, HashMap<String, String>> flat = new Gson().fromJson(data, new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType());
        for (String name : flat.keySet()) {
            Module module = ComoClient.getInstance().getModules().get(name);
            
            // Check that we can get the module
            if (module == null) {
                ComoClient.log(String.format("Unable to find module '%s,' ignoring...", name));
                continue;
            }

            module.lift(flat.get(name));
        }

        return true;
    }

    public static String makeConfig() {
        Gson gson = new Gson();

        // Save all of the modules
        HashMap<String, HashMap<String, String>> flat = new HashMap<String, HashMap<String, String>>();
        for (String name : ComoClient.getInstance().getModules().keySet()) {
            Module module = ComoClient.getInstance().getModules().get(name);

            flat.put(name, module.flatten());
        }

        // TODO Save friends list

        return gson.toJson(flat);
    }

    public static void saveConfig() {
        String json = makeConfig();
        try {
            writeConfig(json, CONFIG_PATH);
        } catch (IOException e) {
            ComoClient.log(e);
        }
    }
}
