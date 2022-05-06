package net.como.client.gui;

import imgui.ImGui;

public class ClickGUIScreen extends ImGuiScreen {
    @Override
    protected void renderImGui() {
        ImGui.begin("ClickGUI");

        ImGui.text("Hello World!");
        ImGui.button("Hey there!", ImGui.getWindowWidth() - 16, 16);
        
        ImGui.end();
    }
}
