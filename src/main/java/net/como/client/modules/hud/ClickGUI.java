package net.como.client.modules.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.OnMouseButtonEvent;
import net.como.client.gui.menu.BlockTitle;
import net.como.client.gui.menu.MenuBlock;
import net.como.client.gui.menu.ModBlockTile;
import net.como.client.gui.menu.structures.MenuBlockTile;
import net.como.client.structures.Colour;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.Render2DUtils;
import net.minecraft.util.math.Vec2f;

public class ClickGUI extends Module {
    float scaleFactor = 1.5f;

    // TODO make a screen haha

    public ClickGUI() {
        super("ClickGUI");
        this.description = "CURRENTLY JUST A TEST MODULE - ignore this please.";

        // Like this is temp
        this.addSetting(new Setting("Spacing", 15));
        this.addSetting(new Setting("Scale", 1d));
        this.addSetting(new Setting("BlockWidth", 150));

        this.setCategory("HUD");
    }

    private List<MenuBlock> menuBlocks = new ArrayList<>();

    private void populateMenuBlocks() {
        this.scaleFactor = (float)(double)this.getDoubleSetting("Scale");

        menuBlocks.clear();

        // Let's get the categories
        HashMap<String, List<Module>> categories = new HashMap<>();

        for (Module mod : ComoClient.Modules.values()) {
            String cat = mod.getCategory();

            categories.putIfAbsent(cat, new ArrayList<Module>());
            categories.get(cat).add(mod);
        }

        // To start, we will place it 15 pixels into the screen.
        int x = 15;

        // this will be the default spacing between the blocks (if they don't get moved)
        int spacing = this.getIntSetting("Spacing");

        // This will be the width of the menu blocks
        int boxWidth = this.getIntSetting("BlockWidth");

        // TODO add it so the user can move these around trivially.

        for (String categoryName : categories.keySet()) {
            List<Module> category = categories.get(categoryName);

            // Create a menu block
            MenuBlock block = new MenuBlock(
                new Vec2f(x, 15),
                new Vec2f(boxWidth, MenuBlock.calculateHeight(category.size() + 1, this.scaleFactor))
            );

            block.setScaleFactor(this.scaleFactor);

            // Add a title
            new BlockTitle(block, categoryName);

            // Add the modules to the list.
            for (Module mod : category) {
                new ModBlockTile(block, mod);
            }

            // Add the block
            this.menuBlocks.add(block);

            // Calculate where we are going to place the next block
            x += boxWidth + spacing;
        }
    }

    @Override
    public void activate() {
        this.displayMessage("This is a test module, it currently serves no purpose.");

        // TODO use a different thing - such as like a screen or something.
        this.addListen(InGameHudRenderEvent.class);
        this.addListen(OnMouseButtonEvent.class);
        this.populateMenuBlocks();
    }

    @Override
    public void deactivate() {
        this.removeListen(OnMouseButtonEvent.class);
        this.removeListen(InGameHudRenderEvent.class);
    }

    // This is included in screen so when we move to using a screen, remove this.
    private boolean mouseDown = false;

    @Override
    public void fireEvent(Event event) {
        if (!ClientUtils.inGame()) return;

        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                for (MenuBlock menuBlock : this.menuBlocks) {
                    menuBlock.render(e.mStack);
                }

                break;
            }

            case "OnMouseButtonEvent": {
                OnMouseButtonEvent e = (OnMouseButtonEvent)event;

                // TODO handle right clicks differently

                this.mouseDown = !mouseDown;

                if (!this.mouseDown) break;

                for (MenuBlock menuBlock : this.menuBlocks) {
                    if (menuBlock.isMouseOver() && menuBlock.isClickable()) {
                        menuBlock.clicked();

                        e.ci.cancel();

                        break;
                    }
                }
                
                break;
            }
        }
    }
}
