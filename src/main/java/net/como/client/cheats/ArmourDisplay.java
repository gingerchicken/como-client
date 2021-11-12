package net.como.client.cheats;

import java.util.ArrayList;
import java.util.List;

import net.como.client.CheatClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.Window;
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

        // The width of the item on screen
        int width = 18;

        // Get the armour
        List<ItemStack> armour = new ArrayList<ItemStack>();

        // Iterate backwards over the armour set
        for (int i = 3; i >= 0; i--) {
            // Get the piece
            ItemStack piece = CheatClient.me().getInventory().getArmorStack(i);

            // If the piece is nothing and we don't want to render nothing, don't add it to the list to be rendered.
            if (!piece.isEmpty() || renderEmpty) {
                armour.add(piece);
            }
        }

        // Get the total size
        int totalWidth = armour.size()*width;

        // Calculate the padding to "centre" the items.
        int padLeft = (length - totalWidth) / 2;

        // Get the position where we are going to render the items.
        int curX  = x + padLeft;
        int curY  = y;

        // Render each item
        for (ItemStack item : armour) {
            // Render the item on the screen
            ir.renderInGuiWithOverrides(item, curX, curY);
            ir.renderGuiItemOverlay(r, item, curX, curY);

            // Calculate where we are going to render the next item
            curX += width;
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                Window window = CheatClient.getClient().getWindow();

                int x = window.getScaledWidth() / 2 + 5;
                int y = window.getScaledHeight() - 55;

                if (CheatClient.me().getAir() < 300) y -= 10;
                System.out.println(CheatClient.me().getAir());

                this.renderDisplay(x, y, 91);

                break;
            }
        }
    }
}
