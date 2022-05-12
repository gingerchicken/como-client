package net.como.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

import net.como.client.ComoClient;
import net.como.client.structures.Module;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ChatUtils;

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
            // Show collapse button
            ImGui.begin(cat);

            for (Module mod : categories.get(cat)) {
                // Render the button

                ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 0.0f, 0.5f);

                // Push enabled style
                if (mod.isEnabled()) {
                    ImGui.pushStyleColor(ImGuiCol.Button, 0.10f, 0.60f, 0.10f, 0.75f);
                    ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.10f, 0.50f, 0.10f, 0.75f);
                    ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.30f, 0.70f, 0.30f, 0.75f);
                }

                boolean shouldToggle = ImGui.button(mod.getName(), ImGui.getWindowWidth() - ImGui.getStyle().getWindowPaddingX()*2, 0);

                // Pop enabled style
                if (mod.isEnabled()) {
                    ImGui.popStyleColor(3);
                }

                ImGui.popStyleVar(1);

                // Handle outputs

                // Draw button that toggles the module
                if (shouldToggle) {
                    ChatUtils.hideNextChat = true;
                    mod.toggle();
                }
            }

            ImGui.end();
        }
    }
}
