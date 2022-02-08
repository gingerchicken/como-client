package net.como.client.modules.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.gui.ClickGUIScreen;
import net.como.client.gui.menu.BlockTitle;
import net.como.client.gui.menu.MenuBlock;
import net.como.client.gui.menu.ModBlockTile;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

public class ClickGUI extends Module {
    float scaleFactor = 1.5f;

    public ClickGUI() {
        super("ClickGUI");
        this.description = "A way of toggling your settings with a GUI (Currently WIP)";

        // Like this is temp
        this.addSetting(new Setting("Spacing", 15));
        this.addSetting(new Setting("VerticalSpacing", 5));
        this.addSetting(new Setting("Scale", 1d));
        this.addSetting(new Setting("BlockWidth", 90));

        this.addSetting(new Setting("Bouncy", false));
        this.addSetting(new Setting("BouncySpeed", 1d));
        this.addSetting(new Setting("TotalBouncies", 1));

        this.setCategory("HUD");
    }

    // TODO move this to the screen
    private List<MenuBlock> menuBlocks = new ArrayList<>();

    public void populateMenuBlocks() {
        this.scaleFactor = (float)(double)this.getDoubleSetting("Scale");

        menuBlocks.clear();

        // Let's get the categories
        HashMap<String, List<Module>> categories = new HashMap<>();

        for (Module mod : ComoClient.Modules.values()) {
            String cat = mod.getCategory();

            categories.putIfAbsent(cat, new ArrayList<Module>());
            categories.get(cat).add(mod);
        }

        int sidePadding = 15;
        int topPadding  = 15;

        // To start, we will place it 15 pixels into the screen.
        int x = sidePadding;

        // this will be the default spacing between the blocks (if they don't get moved)
        int spacing = this.getIntSetting("Spacing");

        // This will be the width of the menu blocks
        int boxWidth = this.getIntSetting("BlockWidth");

        int windowWidth = ComoClient.getClient().getWindow().getScaledWidth();

        int totalRows = (int)((windowWidth - sidePadding) / (spacing + boxWidth));

        List<Integer> heights = new ArrayList<>();

        // TODO add it so the user can move these around trivially.
        // This should just be the setup!

        int i = 0;
        for (String categoryName : categories.keySet()) {
            List<Module> category = categories.get(categoryName);

            // Get the menu block's height
            Vec2f blockHeight = new Vec2f(boxWidth, MenuBlock.calculateHeight(category.size() + 1, this.scaleFactor));

            // Wrap i around the rows
            i = i % totalRows;

            // Reset x if needed
            x = i == 0 ? sidePadding : x;

            // Get the last height or just add the current one.
            Boolean firstHeight = false;
            if (heights.size() <= i) {
                heights.add(sidePadding + topPadding);
                firstHeight = true;
            }

            // Calculate y position
            int y = heights.get(i);
            y = firstHeight ? y : y + this.getIntSetting("VerticalSpacing");
            heights.set(i, y + (int)blockHeight.y);

            Vec2f pos = new Vec2f(x, y);

            // Create a menu block
            MenuBlock block = new MenuBlock(pos, blockHeight);

            // Set the scale factor
            block.setScaleFactor(this.scaleFactor);

            // Add a title
            new BlockTitle(block, categoryName);

            // Add the modules to the list.
            for (Module mod : category) {
                new ModBlockTile(block, mod);
            }

            // Sort the list
            block.sortAlphabetically();

            // Add the block
            this.menuBlocks.add(block);

            // Calculate where we are going to place the next block
            x += boxWidth + spacing;

            // Increment the column
            i++;
        }
    }

    ClickGUIScreen screen;

    @Override
    public void activate() {
        this.displayMessage("This is a test module, it currently serves no purpose.");

        this.screen = new ClickGUIScreen(this);

        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        if (ComoClient.getClient().currentScreen instanceof ClickGUIScreen) {
            ComoClient.getClient().setScreen(null);
        }

        this.removeListen(ClientTickEvent.class);
    }

    public void renderMenuBlocks(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (MenuBlock menuBlock : this.menuBlocks) {
            menuBlock.render(matrices, mouseX, mouseY, delta);
        }
    }

    public void tickMenuBlocks() {
        for (MenuBlock menuBlock : this.menuBlocks) {
            menuBlock.tick();
        }
    }

    public boolean handleClick(int button) {
        for (MenuBlock menuBlock : this.menuBlocks) {
            if (menuBlock.isMouseOver() && menuBlock.isClickable()) {
                menuBlock.clicked();

                return true;
            }
        }

        return false;
    }

    public void applySearchPhrase(String phrase) {
        for (MenuBlock block : this.menuBlocks) {
            block.setSearchPhrase(phrase);
        }
    }

    @Override
    public void fireEvent(Event event) {
        // TODO this is due to the fact that the activate command is called before the closeScreen function.
        // Basically, make it so that activate triggers after the chatscreen is closed.
        if (ComoClient.getClient().currentScreen == null) {
            ComoClient.getClient().setScreen(screen);
        }
    }
}
