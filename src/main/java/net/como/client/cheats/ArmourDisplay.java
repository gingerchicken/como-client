package net.como.client.cheats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.ArrayUtils;

import net.como.client.CheatClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;

public class ArmourDisplay extends Cheat {

    public ArmourDisplay() {
        super("ArmourDisplay");

        this.addSetting(new Setting("RenderEmpty", false));
    }

    @Override
    public void activate() {
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
    }

    private void renderDisplay(int x, int y, int length) {
        MinecraftClient client = CheatClient.getClient();
        ItemRenderer ir = client.getItemRenderer();
        TextRenderer r  = client.textRenderer;

        Boolean renderEmpty = (Boolean)this.getSetting("RenderEmpty").value;

        int width = 18;

        List<ItemStack> armour = new ArrayList<ItemStack>();

        for (int i = 3; i >= 0; i--) {
            ItemStack piece = CheatClient.me().getInventory().getArmorStack(i);

            if (!piece.isEmpty() || renderEmpty) {
                armour.add(piece);
            }
        }

        int totalWidth = armour.size()*width;

        int padLeft = (length - totalWidth) / 2;

        int curX  = x + padLeft;
        int curY  = y;

        // Render each item
        for (ItemStack item : armour) {
            ir.renderInGuiWithOverrides(item, curX, curY);
            ir.renderGuiItemOverlay(r, item, curX, curY);

            curX += width;
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;
                
                this.renderDisplay(CheatClient.getClient().getWindow().getScaledWidth()/2 + 5, CheatClient.getClient().getWindow().getScaledHeight() - 55, 91);
                

                break;
            }
        }
    }
}
