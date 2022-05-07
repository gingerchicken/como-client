package net.como.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import imgui.ImGui;
import net.como.client.ComoClient;
import net.como.client.structures.Module;
import net.como.client.structures.settings.Setting;

public class ClickGUIScreen extends ImGuiScreen {
    private HashMap<String, List<Module>> categories = new HashMap<>();
    
    @Override
    protected void init() {
        super.init();
        categories.clear();

        for (Module mod : ComoClient.Modules.values()) {
            String cat = mod.getCategory();

            categories.putIfAbsent(cat, new ArrayList<Module>());
            categories.get(cat).add(mod);
        }
    }
    
    @Override
    protected void renderImGui() {
        for (String cat : categories.keySet()) {
            ImGui.begin(cat);

            for (Module mod : categories.get(cat)) {
                boolean show = ImGui.collapsingHeader(mod.getName());

                if (!show) continue;

                // Display the mod description
                ImGui.text(mod.description);

                boolean t = mod.isEnabled();
                ImGui.checkbox("enable", t);

                if (t != mod.isEnabled()) {
                    mod.toggle();
                }

                ImGui.separator();

                // Display the mod's settings
                for (String settingName : mod.getSettings()) {
                    Setting setting = mod.getSetting(settingName);

                    Object obj = setting.value;
                }
            }

            ImGui.end();
        }
    }
}
