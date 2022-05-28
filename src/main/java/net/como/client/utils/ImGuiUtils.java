package net.como.client.utils;

/**
 * Shout out to 0x150 for help with the ImGuiUtils class.
 * A lot of the code is from his ImGuiManager class in Atomic Client!
 */

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.como.client.ComoClient;
import net.como.client.gui.ImGuiScreen;

import imgui.type.ImDouble;
import imgui.type.ImString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImGuiUtils {
    /**
     * Path to the default font.
     */
    private static final String FONT_PATH = "assets/como-client/font/como.ttf";

    private static final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();

    /**
     * Has ImGui been initialised
     */
    private static boolean initised = false;

    /**
     * Gets if the ImGui has been initialised.
     * @return if the ImGui has been initialised.
     */
    public static boolean isInitalised() {
        return initised;
    }

    /**
     * Get the Gl3 implementation
     * @return the Gl3 implementation
     */
    public static ImGuiImplGl3 getImplGl3() {
        return implGl3;
    }

    /**
     * Get the Glfw implementation
     * @return the Glfw implementation
     */
    public static ImGuiImplGlfw getImplGlfw() {
        return implGlfw;
    }

    /**
     * Reads the font from the resources folder
     * @return The font as a byte array
     */
    private static byte[] getMainFont() {
        try {
            return Files.readAllBytes(Paths.get(ImGuiScreen.class.getClassLoader().getResource(FONT_PATH).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the ImGui style
     */
    public static void refreshStyle() {
        // TODO this style was taken from Atomic client, please customise it to make it look even better!

        ImGuiStyle style = ImGui.getStyle();

        // Window style
        style.setWindowPadding(4, 4);

        style.setFramePadding(4, 4);
        style.setCellPadding(4, 2);
        style.setItemSpacing(8, 4);
        style.setItemInnerSpacing(4, 4);
        style.setTouchExtraPadding(0, 0);
        style.setIndentSpacing(21);
        style.setScrollbarSize(15);
        style.setGrabMinSize(4);

        style.setWindowRounding(5);
        style.setChildRounding(1);
        style.setFrameRounding(4);
        style.setPopupRounding(1);
        style.setScrollbarRounding(1);
        style.setGrabRounding(1);
        style.setLogSliderDeadzone(4);
        style.setTabRounding(4);

        // Text
        style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f);

        // Backgrounds
        style.setColor(ImGuiCol.WindowBg, 0.10f, 0.10f, 0.10f, 0.94f);
        style.setColor(ImGuiCol.ChildBg, 0.10f, 0.10f, 0.10f, 0.94f);
        style.setColor(ImGuiCol.PopupBg, 0.10f, 0.10f, 0.10f, 1.00f);
        style.setColor(ImGuiCol.Border, 0.10f, 0.10f, 0.10f, 0.94f);

        // Window Title
        float k = 0.50f;
        style.setColor(ImGuiCol.TitleBg, k, k, k, 0.7f);
        style.setColor(ImGuiCol.TitleBgActive, k + 0.05f, k + 0.05f, k + 0.05f, 0.80f);
        style.setColor(ImGuiCol.TitleBgCollapsed, k, k, k, 0.80f);

        // Scrollbar
        style.setColor(ImGuiCol.ScrollbarBg, 0.07f, 0.12f, 0.13f, 0.94f);
        style.setColor(ImGuiCol.ScrollbarGrab, 0.20f, 0.50f, 0.35f, 0.50f);
        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.28f, 0.54f, 0.41f, 0.50f);
        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.20f, 0.50f, 0.28f, 0.50f);

        style.setColor(ImGuiCol.BorderShadow, 0.00f, 0.00f, 0.00f, 1.00f);

        // Frames (i.e. text box, checkbox, etc)
        style.setColor(ImGuiCol.FrameBg, 0.5f, 0.5f, 0.5f, 0.25f);
        style.setColor(ImGuiCol.FrameBgHovered, 0.5f, 0.5f, 0.5f, 0.25f);
        style.setColor(ImGuiCol.FrameBgActive, 0.5f, 0.5f, 0.5f, 0.25f);
        
        style.setColor(ImGuiCol.MenuBarBg, 0.14f, 0.14f, 0.14f, 0.00f);
        style.setColor(ImGuiCol.CheckMark, 0.10f, 0.60f, 0.10f, 1f);
        
        // Slider
        style.setColor(ImGuiCol.SliderGrab, 0.24f, 0.52f, 0.88f, 1.00f);
        style.setColor(ImGuiCol.SliderGrabActive, 0.26f, 0.59f, 0.98f, 1.00f);
        
        // Button
        style.setColor(ImGuiCol.Button, 0.00f, 0.00f, 0.00f, 0.50f);
        style.setColor(ImGuiCol.ButtonHovered, 0.00f, 0.00f, 0.00f, 0.76f);
        style.setColor(ImGuiCol.ButtonActive, 0.10f, 0.10f, 0.10f, 0.76f);
        
        // Header
        style.setColor(ImGuiCol.Header, 0.15f, 0.22f, 0.31f, 0.31f);
        style.setColor(ImGuiCol.HeaderHovered, 0.16f, 0.28f, 0.41f, 0.31f);
        style.setColor(ImGuiCol.HeaderActive, 0.25f, 0.35f, 0.47f, 0.31f);
        
        // Separator
        style.setColor(ImGuiCol.Separator, 0.5f, 0.5f, 0.5f, 0.25f);
        style.setColor(ImGuiCol.SeparatorHovered, 0.10f, 0.40f, 0.75f, 0.78f);
        style.setColor(ImGuiCol.SeparatorActive, 0.10f, 0.40f, 0.75f, 1.00f);
        
        // Resize Grip
        style.setColor(ImGuiCol.ResizeGrip, 0.16f, 0.16f, 0.16f, 0.25f);
        style.setColor(ImGuiCol.ResizeGripHovered, 0.16f, 0.16f, 0.16f, 0.25f);
        style.setColor(ImGuiCol.ResizeGripActive, 0.16f, 0.16f, 0.16f, 0.25f);
        
        // Tabs
        style.setColor(ImGuiCol.Tab, 0.10f, 0.19f, 0.20f, 0.86f);
        style.setColor(ImGuiCol.TabHovered, 0.12f, 0.25f, 0.26f, 0.86f);
        style.setColor(ImGuiCol.TabActive, 0.12f, 0.26f, 0.24f, 0.86f);
        style.setColor(ImGuiCol.TabUnfocused, 0.07f, 0.10f, 0.15f, 0.97f);
        style.setColor(ImGuiCol.TabUnfocusedActive, 0.14f, 0.26f, 0.42f, 1.00f);
        
        // Docking
        style.setColor(ImGuiCol.DockingPreview, 0.26f, 0.59f, 0.98f, 0.70f);
        style.setColor(ImGuiCol.DockingEmptyBg, 0.15f, 0.18f, 0.19f, 1.00f);
        
        // Lines
        style.setColor(ImGuiCol.PlotLines, 0.61f, 0.85f, 0.80f, 1.00f);
        style.setColor(ImGuiCol.PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f);
        
        // Tables
        style.setColor(ImGuiCol.TableHeaderBg, 0.19f, 0.19f, 0.20f, 1.00f);
        style.setColor(ImGuiCol.TableBorderStrong, 0.31f, 0.31f, 0.35f, 1.00f);
        style.setColor(ImGuiCol.TableBorderLight, 0.23f, 0.23f, 0.25f, 1.00f);
        style.setColor(ImGuiCol.TableRowBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.TableRowBgAlt, 1.00f, 1.00f, 1.00f, 0.06f);
        
        // Text Selection
        style.setColor(ImGuiCol.TextSelectedBg, 0.26f, 0.98f, 0.61f, 0.05f);
        style.setColor(ImGuiCol.DragDropTarget, 1.00f, 1.00f, 0.00f, 0.90f);
        
        // Navigation
        style.setColor(ImGuiCol.NavHighlight, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.NavWindowingHighlight, 1.00f, 1.00f, 1.00f, 0.70f);
        style.setColor(ImGuiCol.NavWindowingDimBg, 0.80f, 0.80f, 0.80f, 0.20f);
        style.setColor(ImGuiCol.ModalWindowDimBg, 0.80f, 0.80f, 0.80f, 0.35f);

        // Other configurations
        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        style.setWindowMenuButtonPosition(1);
    }

    /**
     * Initalises the ImGui system.
     */
    public static void init() {
        if (isInitalised()) {
            return;
        }

        // Set that the ImGui has been initalised
        initised = true;

        // Get the window handle
        long handle = ComoClient.getClient().getWindow().getHandle();
        
        // Create the ImGui context
        ImGui.createContext();

        // Set the style/font
        initFonts();
        refreshStyle();

        // Initialise GL
        implGlfw.init(handle, true);
        implGl3.init();
    }

    /**
     * Gets the default font and loads it into the ImGui system.
     */
    public static void initFonts() {
        ImGui.getIO().getFonts().addFontFromMemoryTTF(getMainFont(), 18);
    }

    public static boolean accurateDoubleInput(String label, ImDouble value, String numericalFormat) {
        String formatted = String.format(numericalFormat, value.get());

        // Render the input
        ImString str = new ImString(formatted, 128);

        // Check if it changed
        if (ImGui.inputText(label, str)) {
            String entered = str.toString();

            // Replace all , with . (as some parts of the world use , as a decimal separator)
            entered = entered.replace(',', '.');

            // Replace duplicate decimal points
            entered = entered.replaceAll("\\.+", ".");

            // Replace all non-numerical characters with nothing (except for the decimal point and minus sign)
            entered = entered.replaceAll("[^\\d\\.\\-]", "");

            double d;
            try {
                d = Double.parseDouble(entered);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            value.set(d);

            return true;
        }

        return false;
    }
}
