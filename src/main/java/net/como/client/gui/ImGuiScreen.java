package net.como.client.gui;

import imgui.ImGui;
import net.como.client.ComoClient;
import net.como.client.utils.ImGuiUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class ImGuiScreen extends Screen {
    public static boolean imguiDebugWindow = false;
    private boolean closed = false;
    private boolean closedAck = false;

    public ImGuiScreen() {
        super(Text.of(""));
        ImGuiUtils.init();
    }

    /**
     * Draw ImGui items
     */
    protected abstract void renderImGui();

    @Override
    protected void init() {
        closed = closedAck = false;
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Handle closure
        if (this.closed && this.closedAck) {
            super.close();
            return;
        }

        // Sets the size of the window in case it got resized
        ImGui.getIO().setDisplaySize(this.width, this.height);

        // New ImGui frame
        ImGuiUtils.getImplGlfw().newFrame();
        ImGui.newFrame();

        // Check for closure
        if (!this.closed) {
            // Show metrics window if need be
            if (imguiDebugWindow) ImGui.showMetricsWindow();

            // Render the subclass' content
            this.renderImGui();
        } else {
            // Render an empty frame when closed
            closedAck = true;
        }

        // Finish the frame
        ImGui.endFrame();
        
        // Render the frame
        ImGui.render();
        ImGuiUtils.getImplGl3().renderDrawData(ImGui.getDrawData());
    }
}