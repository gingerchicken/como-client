package net.como.client.modules;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.ComoClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.events.RenderTooltipEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ShulkerPeak extends Module {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier("como-client", "textures/gui/shulker_tooltip_header.png");
    private static final int BACKGROUND_WIDTH = 176, BACKGROUND_HEIGHT = 78;

    public ShulkerPeak() {
        super("ShulkerPeak");

        this.addSetting(new Setting("HUDOverlay", false));
        
        this.description = "Displays the contents of shulkers without opening them.";
    }

    @Override
    public void activate() {
        this.addListen(RenderTooltipEvent.class);
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderTooltipEvent.class);
        this.removeListen(InGameHudRenderEvent.class);
    }

    private List<ItemStack> getItems(ItemStack stack) {
        NbtCompound lv = stack.getSubNbt("BlockEntityTag");

        List<ItemStack> items = new ArrayList<ItemStack>();
        if (lv == null) return items;

        if (lv.contains("Items", 9)) {
            DefaultedList<ItemStack> is = DefaultedList.ofSize(27, ItemStack.EMPTY);
            Inventories.readNbt(lv, is);

            for (ItemStack item : is) {
                items.add(item);
            }
        }

        return items;
    }

    private void renderBackground(MatrixStack matrices, ItemStack stack, int x, int y) {
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

        DrawableHelper.drawTexture(matrices, x, y, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        TextRenderer r = ComoClient.getClient().textRenderer;

        // r.draw(matrices, stack.getName(), x + 7, y - 4, 0xFF404040);
        r.draw(matrices, stack.getName(), x + 7, y + 6, 0xFF404040);
    }

    private void renderShulkerDisplay(MatrixStack mStack, ItemStack stack, int x, int y) {
        List<ItemStack> items = this.getItems(stack);
        ItemRenderer ir = ComoClient.getClient().getItemRenderer();
        TextRenderer r = ComoClient.getClient().textRenderer;

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        // Render background
        this.renderBackground(mStack, stack, x, y);

        // Item offsets
        x += 8;
        y += 18;

        int spacing = 18;
        int shulkerWidth = 9;
        int i = 0;
        for (ItemStack item : items) {
            int curX = x + (i % shulkerWidth) * spacing;
            int curY = y + (i / 9)*spacing;

            ir.renderInGuiWithOverrides(item, curX, curY);
            ir.renderGuiItemOverlay(r, item, curX, curY);

            i++;
        }
    }

    private boolean isShulkerBox(ItemStack stack) {
        Item item = stack.getItem();

        // Make sure that it was a block
        if (!(item instanceof BlockItem)) return false;

        // Make sure it is a shulker
        Block block = ((BlockItem)item).getBlock();

        // Return if it is a shulker
        return block instanceof ShulkerBoxBlock;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderTooltipEvent": {
                RenderTooltipEvent e = (RenderTooltipEvent)event;

                if (!this.isShulkerBox(e.stack)) break;

                // Cancel the old tool tip
                e.ci.cancel();

                // Offset where we are going to render the tooltip
                int x = e.x + 8;
                int y = e.y - 16;

                // Render the tool tip
                this.renderShulkerDisplay(e.mStack, e.stack, x, y);

                break;
            }
            case "InGameHudRenderEvent": {
                if (!(boolean)this.getSetting("HUDOverlay").value) break;
                
                // Get the active item.
                ItemStack stack = ComoClient.me().getMainHandStack();

                // Make sure that it is a shulker
                if (!this.isShulkerBox(stack)) break;

                // Get some event data
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                // Render the shulker box
                int x = ComoClient.getClient().getWindow().getScaledWidth()/2 - BACKGROUND_WIDTH/2;
                int y = (int)(32/ComoClient.getClient().getWindow().getScaleFactor());

                this.renderShulkerDisplay(e.mStack, stack, x, y);

                break;
            }
        }
    }
}
