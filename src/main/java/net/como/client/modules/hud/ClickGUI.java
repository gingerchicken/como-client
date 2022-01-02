package net.como.client.modules.hud;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.gui.menu.BlockTitle;
import net.como.client.gui.menu.MenuBlock;
import net.como.client.gui.menu.ModBlockTile;
import net.como.client.gui.menu.structures.MenuBlockTile;
import net.como.client.structures.Colour;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.utils.Render2DUtils;
import net.minecraft.util.math.Vec2f;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI");
        this.description = "CURRENTLY JUST A TEST MODULE - ignore this please.";
    }

    private List<MenuBlock> menuBlocks = new ArrayList<>();

    private void populateMenuBlocks() {
        menuBlocks.clear();

        String catagories[] = {"Test Category"};

        int padding = 50;
        int spacing = 15;

        int boxWidth = 100;

        int x = padding;

        for (int i = 0; i < catagories.length; i++) {
            int totalMods = 16; // TODO GET THIS WHEN YOU HAVE CATAGORIES

            MenuBlock block = new MenuBlock(new Vec2f(x, 15), new Vec2f(boxWidth, MenuBlock.calculateHeight(totalMods + 2)));
            new BlockTitle(block, catagories[i]);

            int j = 0;
            for (Module mod : ComoClient.Modules.values()) {
                new ModBlockTile(block, mod);

                if (j >= 16) break;
                j++;
            }

            this.menuBlocks.add(block);
            x += boxWidth + spacing;
        }
    }

    @Override
    public void activate() {
        this.displayMessage("This is a test module, it currently serves no purpose.");

        // TODO use a different thing - such as like a screen or something.
        this.addListen(InGameHudRenderEvent.class);
        this.populateMenuBlocks();
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                for (MenuBlock menuBlock : this.menuBlocks) {
                    menuBlock.render(e.mStack);
                }

                break;
            }
        }
    }
}
