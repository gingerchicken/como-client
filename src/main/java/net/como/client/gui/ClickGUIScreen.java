package net.como.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
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

    private static HashMap<Module, Boolean> openedSettings = new HashMap<>();

    /**
     * States if the settings should be rendered for a given module
     * @return if the settings should be rendered
     */
    public boolean shouldShowSettings(Module module) {
        return openedSettings.containsKey(module) && openedSettings.get(module);
    }

    /**
     * Hides the settings for the given module
     * @param module
     */
    public void hideSettings(Module module) {
        // Remove the module from the opened settings hashmap
        openedSettings.remove(module);
    }

    /**
     * Flags a given module to have its settings rendered
     * @param module the module to render the settings for
     */
    public void showSettings(Module module) {
        openedSettings.put(module, true);
    }

    private static HashMap<Class<?>, Boolean> renderableSettingTypes = new HashMap<>() {{
        put(Boolean.class, true);
        put(String.class, true);
        // put(Integer.class, true);
        // put(Double.class, true);
    }};

    /**
     * Checks if a given setting is supported and can be rendered
     * @param setting the setting to check
     * @return if the setting can be rendered
     */
    private boolean canSettingBeRender(Setting setting) {
        return renderableSettingTypes.containsKey(setting.value.getClass());
    }

    private boolean renderSetting(Setting setting) {
        if (!this.canSettingBeRender(setting)) return false;
    
        // Handle different types of settings
        switch (setting.value.getClass().getSimpleName()) {
            // Handle Booleans
            case "Boolean": {
                if (ImGui.checkbox(setting.name, (Boolean)setting.value)) {
                    setting.value = !(Boolean)setting.value;
                }
                break;
            }

            // Handle strings
            case "String": {
                ImString str = new ImString((String) setting.value);
                ImGui.inputText(setting.name, str);
                setting.value = str.toString();

                break;
            }

            default: {
                return false;
            }
        }


        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(setting.getToolTip());
        }

        return true;
    }

    /**
     * Toggles the settings from being rendered for a given module
     * @param module the module to toggle the settings for
     * @return if the settings are now being rendered
     */
    public boolean toggleSettings(Module module) {
        // Hide the module is already added then remove it to hide it
        if (this.shouldShowSettings(module)) {
            this.hideSettings(module);
            return false;
        }

        // Otherwise add the module to the opened settings hashmap
        this.showSettings(module);
        return true;
    }

    @Override
    protected void renderImGui() {
        for (String cat : categories.keySet()) {
            // Show collapse button
            ImGui.begin(cat);

            for (Module mod : categories.get(cat)) {
                // Render the button

                // Push style variable
                ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 0.0f, 0.5f);

                // Push enabled style
                if (mod.isEnabled()) {
                    ImGui.pushStyleColor(ImGuiCol.Button, 0.10f, 0.60f, 0.10f, 0.75f);
                    ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.10f, 0.50f, 0.10f, 0.75f);
                    ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.30f, 0.70f, 0.30f, 0.75f);
                }

                boolean shouldToggle = ImGui.button(mod.getName(), ImGui.getWindowWidth() - ImGui.getStyle().getWindowPaddingX()*2, 0);
                boolean hasSettings = !mod.getSettings().isEmpty();

                // Handle if the mouse is being hovered
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip(mod.description == null ? "No description, sorry :(" : mod.description);

                    // Handle right clicks
                    if (ImGui.isMouseClicked(1) && hasSettings) {
                        this.toggleSettings(mod);
                    }
                }

                // Render the settings if they are to be rendered
                if (this.shouldShowSettings(mod) && hasSettings) {
                    ImGui.separator();
                    for (String settingName : mod.getSettings()) {
                        Setting setting = mod.getSetting(settingName);

                        this.renderSetting(setting);
                    }
                    ImGui.separator();
                }

                // Pop enabled style
                if (mod.isEnabled()) {
                    ImGui.popStyleColor(3);
                }

                // Pop the style variable
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
