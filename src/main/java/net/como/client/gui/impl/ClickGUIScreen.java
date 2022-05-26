package net.como.client.gui.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.system.CallbackI.F;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImDouble;
import imgui.type.ImInt;
import imgui.type.ImString;
import joptsimple.internal.Strings;
import net.como.client.ComoClient;
import net.como.client.gui.ImGuiScreen;
import net.como.client.gui.impl.widgets.BouncyWidget;
import net.como.client.misc.Colour;
import net.como.client.misc.Mode;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.como.client.modules.hud.ClickGUI;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.ImGuiUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUIScreen extends ImGuiScreen {
    private HashMap<String, List<Module>> categories = new HashMap<>();
    private List<BouncyWidget> bouncyWidgets = new ArrayList<>();
    private String searchExample;
    
    private double getBouncySpeed() {
        return this.getClickGUI().getDoubleSetting("BouncySpeed");
    }

    private void syncBouncySettings() {
        // Check if the bouncies are enabled.
        if (!this.getClickGUI().getBoolSetting("Bouncy")) {
            this.bouncyWidgets.clear();
            return;
        }

        // Check the total amount of felixes.
        int totalFelixes = this.getClickGUI().getIntSetting("TotalBouncies");

        // Get the bouncy speed.
        double bouncySpeed = this.getBouncySpeed();

        // Make sure that they're the same amount.
        while (this.bouncyWidgets.size() < totalFelixes) {
            // Add a new felix.
            this.bouncyWidgets.add(new BouncyWidget(this.width, this.height, 1d/6, bouncySpeed));
        }

        while (this.bouncyWidgets.size() > totalFelixes) {
            // Remove a felix.
            this.bouncyWidgets.remove(this.bouncyWidgets.size() - 1);
        }

        // Sync the felixes.
        for (BouncyWidget felix : this.bouncyWidgets) {
            felix.setSpeed(bouncySpeed);
        }
    }

    @Override
    protected void init() {
        super.init();

        // Clear the current components
        categories.clear();
        bouncyWidgets.clear();

        // Add all modules to the categories
        for (Module mod : ComoClient.getInstance().getModules().values()) {
            String cat = mod.getCategory();

            categories.putIfAbsent(cat, new ArrayList<Module>());
            categories.get(cat).add(mod);
        }

        // Sync the bouncy settings
        this.syncBouncySettings();

        // Set the search example as random module name
        this.updateSearchExample();
    }

    private void updateSearchExample() {
        // Get a random index
        int i = (int)(Math.random() * ComoClient.getInstance().getModules().size());

        // Iterate and get the module
        for (Module mod : ComoClient.getInstance().getModules().values()) {
            if (i-- == 0) {
                this.searchExample = mod.getName();
                return;
            }
        }

        // Just return an empty string
        this.searchExample = Strings.EMPTY;
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
        put(Integer.class, true);
        put(Double.class, true);
        put(Mode.class, true);
    }};

    /**
     * Checks if a given setting is supported and can be rendered
     * @param setting the setting to check
     * @return if the setting can be rendered
     */
    private boolean canSettingBeRender(Setting setting) {
        return renderableSettingTypes.containsKey(setting.value.getClass());
    }

    private boolean renderSetting(Module mod, Setting setting) {
        if (!this.canSettingBeRender(setting)) return false;

        final String numericalFormat = "%.3f";

        ImGui.pushID(setting.name);

        // "Padding"
        ImGui.spacing();
        ImGui.sameLine();

        // Handle different types of settings
        switch (setting.value.getClass().getSimpleName()) {
            // Handle Booleans
            case "Boolean": {
                if (ImGui.checkbox(setting.name, (Boolean)setting.value)) {
                    setting.value = !(Boolean)setting.value; // It was toggled
                }

                break;
            }

            // Handle Strings
            case "String": {
                ImString str = new ImString((String)setting.value, 128);
                ImGui.inputText(setting.name, str);
                setting.value = str.toString();

                break;
            }

            // Handle Double
            case "Double": {
                // Get the current value
                ImDouble value = new ImDouble((Double)setting.value);

                // Set the width for the bar
                ImGui.pushItemWidth(ImGui.getFontSize() * 6);

                boolean changed = false;
                // Check to see if we have a range
                if (setting.hasRange()) {
                    double min = (double) setting.getMin();
                    double max = (double) setting.getMax();

                    // Render the slider
                    changed = ImGui.sliderScalar(setting.name, ImGuiDataType.Double, value, min, max, numericalFormat);
                } else {
                    changed = ImGuiUtils.accurateDoubleInput(setting.name, value, numericalFormat);
                }

                // Update the value
                if (changed) {
                    setting.value = value.get();
                }

                // Pop the width
                ImGui.popItemWidth();

                break;
            }

            // Handle integers
            case "Integer": {
                ImInt value = new ImInt((Integer)setting.value);

                boolean changed = false;

                ImGui.pushItemWidth(ImGui.getFontSize() * 6);

                if (setting.hasRange()) {
                    int min = (int) setting.getMin();
                    int max = (int) setting.getMax();

                    changed = ImGui.sliderScalar(setting.name, ImGuiDataType.S32, value, min, max);
                } else {
                    changed = ImGui.inputInt(setting.name, value, 0, 0);
                }
                
                if (changed) {
                    setting.value = value.get();
                }

                ImGui.popItemWidth();

                break;
            }

            case "Mode": {
                // Get the current mode
                Mode mode = (Mode) setting.value;

                int currentIndex = 0;

                List<String> modes = new ArrayList<>();
                for (String m : mode.getEntries()) {
                    modes.add(m);
                }

                // Find the current index
                currentIndex = mode.getState();

                // Setup ImInt
                ImInt imInt = new ImInt(currentIndex);

                // Get the string array
                String[] modeStrings = modes.toArray(new String[modes.size()]);

                // Render the combo box
                if (ImGui.combo(setting.name, imInt, modeStrings)) {
                    mode.setState(imInt.get());
                }

                break;
            }

            default: {
                ImGui.popID();
                return false;
            }
        }

        // Show setting tool tip
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(setting.getToolTip());
        }

        ImGui.popID();
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

    public void renderBackground(MatrixStack matrices, float tickDelta) {
        // Current background darkness
        float current = this.backgroundDarkness;

        float next = this.getNext(current, BACKGROUND_DARKNESS_SPEED, BACKGROUND_DARKNESS_MIN, 1.0f);

        // Get lerped background darkness
        float backgroundDarkness = this.lerpValue(current, next, tickDelta);

        // Actual background darkness
        float d = 1 - backgroundDarkness;

        // Get the start and end colours
        Colour s = new Colour(0, 0, 0, 100*d);
        Colour e = new Colour(0, 0, 0, 150*d);

        this.fillGradient(matrices, 0, 0, this.width, this.height, RenderUtils.RGBA2Int(s), RenderUtils.RGBA2Int(e));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private float backgroundDarkness = 1.0f;
    private final static float BACKGROUND_DARKNESS_SPEED = -0.10f;
    private final static float BACKGROUND_DARKNESS_MIN = 0f;

    private float getScale() {
        double scale = this.getClickGUI().getDoubleSetting("Scale");

        return (float) scale;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getClickGUI().isEnabled()) this.close();

        // Handle reset next
        if (resetNext) {
            resetNext = false;
            
            // Clear the opened settings
            openedSettings.clear();
        }

        // Update the global ImGUI scale
        ImGui.getIO().setFontGlobalScale(this.getScale());

        // Background darkness
        if (this.backgroundDarkness > BACKGROUND_DARKNESS_MIN) {
            this.backgroundDarkness += BACKGROUND_DARKNESS_SPEED;
        }

        // Category tones
        if (this.emptyBgTone > EMPTY_BG_TONE_MIN) {
            this.emptyBgTone += EMPTY_BG_TONE_SPEED;
        }

        // Sync the felixes
        this.syncBouncySettings();

        // Bouncy ticks
        for (BouncyWidget widget : this.bouncyWidgets) {
            widget.tick();
        }
    }

    // Search category tones
    private float emptyBgTone = 1f;
    private final static float EMPTY_BG_TONE_SPEED = -0.05f;
    private final static float EMPTY_BG_TONE_MIN = 0.5f;

    /**
     * Gets the next value for a given value, speed and min
     * @param curr the current value
     * @param speed the speed to change the value by
     * @param min the minimum value
     * @param max the maximum value
     * @return a clamped next value
     */
    private float getNext(float curr, float speed, float min, float max) {
        return Math.max(min, Math.min(max, curr + speed));
    }

    private float lerpValue(float current, float next, float tickDelta) {
        return current + (next - current) * tickDelta;
    }

    private static int getSaveCondition() {
        return !resetNext ? ImGuiCond.FirstUseEver : ImGuiCond.Always; 
    }

    /**
     * Creates windows for all the categories and populates them with modules
     */
    private void renderModules(float tickDelta) {        
        // Default padding
        final float xPadding = 15 * this.getScale();
        final float yPadding = 15 * this.getScale();
        final float yOffset  = 80 * this.getScale();

        // For default positioning
        float nextXPos = xPadding;

        // For default width/height
        final float defaultWidth = 175f * this.getScale();

        // Store previous heights
        List<Float> prevHeights = new ArrayList<>();
        int heightPtr = 0;

        float emptyBgTone = this.emptyBgTone;

        // Get next empty background tone
        float next = this.getNext(emptyBgTone, EMPTY_BG_TONE_SPEED, EMPTY_BG_TONE_MIN, 1.0f);

        // Lerp empty background tone
        emptyBgTone = this.lerpValue(this.emptyBgTone, next, tickDelta);

        for (String cat : categories.keySet()) {
            // Get the list of modules for the category as a copy
            List<Module> modules = new ArrayList<>(categories.get(cat));

            // Remove all of the modules from the list that don't contain the search phrase
            if (searchPhrase != null && !searchPhrase.isEmpty()) {
                modules.removeIf(mod -> !mod.getName().toLowerCase().contains(searchPhrase.toLowerCase()));
            }

            // Sort the list alphabetically
            modules.sort((a, b) -> a.getName().compareTo(b.getName()));

            // Check the category is not empty
            if (modules.isEmpty()) {
                // Change the background darkness to make it obvious that the category is empty
                ImGui.setNextWindowBgAlpha(emptyBgTone);
            }

            // First ever positioning
            float yPos = prevHeights.size() < heightPtr + 1 ? yPadding + yOffset : prevHeights.get(heightPtr) + yPadding;

            float currentHeight = (modules.size() + 1) * 30;

            // Set first ever position
            ImGui.setNextWindowSize(defaultWidth, currentHeight, getSaveCondition());
            ImGui.setNextWindowPos(nextXPos, yPos, getSaveCondition());
            
            // Set the window as expanded
            ImGui.setNextWindowCollapsed(false, getSaveCondition());

            // Calculate next positions
            nextXPos += defaultWidth + xPadding;

            // Add the height if the length is less than current pointer
            if (prevHeights.size() < heightPtr + 1) {
                prevHeights.add(yPos + currentHeight);
            } else {
                prevHeights.set(heightPtr, yPos + currentHeight);
            }

            // Increment height pointer
            heightPtr++;

            // Wrap them in case they're off the screen
            if (nextXPos + defaultWidth + xPadding > ComoClient.getClient().getWindow().getWidth()) {
                nextXPos = xPadding;
                heightPtr = 0;
            }

            // Show collapse button
            ImGui.begin(cat);

            for (Module mod : modules) {
                // Push a new id
                ImGui.pushID(mod.getName());

                // Push style variable
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

                boolean hasSettings = !mod.getSettings().isEmpty();

                // Handle if the mouse is being hovered
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip(mod.getDescription() == null ? "No description, sorry :(" : mod.getDescription());

                    // Handle right clicks
                    if (ImGui.isMouseClicked(1) && hasSettings) {
                        this.toggleSettings(mod);
                    }
                }

                // Render the module options
                if (
                    (this.shouldShowSettings(mod) && hasSettings)
                ) {

                    ImGui.separator();

                    // Handle the stupid reset button case
                    if (mod instanceof ClickGUI) {
                        // Render the reset button

                        // Center the button text
                        ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 0.5f, 0.5f);

                        ImGui.spacing();
                        ImGui.sameLine();

                        if (ImGui.button("Reset All", ImGui.getWindowWidth() - ImGui.getStyle().getWindowPaddingX()*2 - ImGui.getStyle().getItemSpacingX()*2, 0)) {
                            resetNext = true;
                        }

                        ImGui.popStyleVar(1);
                    }

                    for (String settingName : mod.getSettings()) {
                        Setting setting = mod.getSetting(settingName);

                        this.renderSetting(mod, setting);
                    }

                    ImGui.separator();
                }

                // Pop the style variable
                ImGui.popStyleVar(1);

                // Handle outputs

                // Draw button that toggles the module
                if (shouldToggle) {
                    ChatUtils.hideNextChat = true;
                    mod.toggle();
                }

                // Pop the id
                ImGui.popID();
            }

            ImGui.end();

            // Reset the mouse count
            ImGui.resetMouseDragDelta();
        }
    }

    /**
     * The current search phrase
    */
    private static String searchPhrase = "";

    public static boolean resetNext = false;

    /**
     * Render the search window
     * @param tickDelta tick delta
     * @return the search phrase
     */
    private String renderSearch(float tickDelta) {
        // Set the default position
        ImGui.setNextWindowPos(this.width - 100f*getScale(), 16* this.getScale(), getSaveCondition());

        // Set the window size
        // Set the scaled window size
        ImGui.setNextWindowSize(200f * this.getScale(), 0);

        // Make it so that the window is just enough to fit a textbox in
        ImGui.setNextWindowContentSize(0, 0f);

        // Begin the window
        ImGui.begin("Search", ImGuiWindowFlags.NoResize);
        // Hover the search button
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("Search for a module");
        }

        ImGui.pushItemWidth(-1);
        // Render the search input
        ImString str = new ImString(searchPhrase, 64);
        ImGui.inputTextWithHint(Strings.EMPTY, "e.g. " + this.searchExample, str);

        // Check for changes in the search phrase
        if (!str.toString().equals(searchPhrase)) {
            // Update the background tone
            this.emptyBgTone = 1f;
        }

        searchPhrase = str.toString();

        ImGui.popItemWidth();

        ImGui.end();

        return searchPhrase;
    }

    @Override
    protected void renderImGui(float tickDelta) {
        this.renderModules(tickDelta);
        this.renderSearch(tickDelta);
    }

    private ClickGUI getClickGUI() {
        return (ClickGUI)ComoClient.getInstance().getModules().get("clickgui");
    }

    @Override
    public void close() {
        // Hide the chat output
        ChatUtils.hideNextChat = true;

        // Disable the module
        this.getClickGUI().disable();

        super.close();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Render background
        this.renderBackground(matrices, delta);

        // Render Bouncy widgets
        for (BouncyWidget widget : this.bouncyWidgets) {
            widget.render(matrices, mouseX, mouseY, delta);
        }

        // Render everything else
        super.render(matrices, mouseX, mouseY, delta);
    }
}
