package net.como.client.gui.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joptsimple.internal.Strings;
import net.como.client.gui.Widget;
import net.como.client.structures.Colour;
import net.como.client.utils.Render2DUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

public class MenuBlock extends Widget {
    public Colour bgColour, outline;
    
    public static int tileSizes     = 14;
    public static int tileSpacing   = 1;
    public static int tilePadding   = 2;

    private String searchPhrase = Strings.EMPTY;
    public String getSearchPhrase() {return this.searchPhrase;}
    public void setSearchPhrase(String phrase) {
        this.searchPhrase = phrase;
    }

    public static int calculateHeight(int totalMods) {
        return (int)((float)(totalMods * tileSpacing + tileSizes * totalMods));
    }

    public static int calculateHeight(int totalMods, float scaleFactor) {
        return (int)((float)calculateHeight(totalMods));
    }
    
    // TODO add scroll bars

    public MenuBlock(Vec2f position, Vec2f size) {
        super(position, size);
    
        this.bgColour = new Colour(0, 0, 0, 150);
        this.outline  = new Colour(0, 0, 0, 255);

        this.clickable = true;
    }

    public void sortAlphabetically() {
        // Get title block and remove it
        BlockTitle title = (BlockTitle)this.popChild();

        // Copy the current widget list
        List<Widget> tiles = new ArrayList<>(this.getChildren());

        // Clear all from current list
        this.getChildren().clear();

        // Sort the current widget list
        Collections.sort(tiles, new Comparator<Widget>() {
            @Override
            public int compare(Widget a, Widget b) {
                ModBlockTile tileA = (ModBlockTile)(a);
                ModBlockTile tileB = (ModBlockTile)(b);

                return tileA.getModule().getName().charAt(0) - tileB.getModule().getName().charAt(0);
            }
        });

        // Add the title first
        this.addChild(title);

        // Add the mods sorted
        int i = 1;
        for (Widget widget : tiles) {
            ModBlockTile tile = (ModBlockTile)(widget);
            tile.index = i++;

            this.addChild(tile);
        }
    }

    @Override
    public void render(MatrixStack matrixStack) {
        Vec2f pos = Render2DUtils.relPosition(this.getScreenPosition());
        Vec2f size = Render2DUtils.relPosition(this.getSize());
        
        Render2DUtils.renderBackgroundBox(
            matrixStack,
            (int)pos.x,
            (int)pos.y,
            (int)(pos.x + size.x),
            (int)(pos.y + size.y - 1), // -1 cus something messed up
            bgColour,
            outline
        );

        String search = this.getSearchPhrase().toLowerCase().strip();
        for (Widget child : this.getChildren()) {
            if (!search.isBlank() && child instanceof ModBlockTile) {
                ModBlockTile modTile = (ModBlockTile)(child);
                
                if (!modTile.getModule().getName().toLowerCase().contains(search)) continue;
            }

            child.render(matrixStack);
        }
    }

}
