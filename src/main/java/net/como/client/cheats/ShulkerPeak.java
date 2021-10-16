package net.como.client.cheats;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.CheatClient;
import net.como.client.events.RenderTooltipEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec2f;

public class ShulkerPeak extends Cheat {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier("como-client", "textures/gui/shulker_tooltip_header.png");

    public ShulkerPeak() {
        super("ShulkerPeak");
    }

    @Override
    public void activate() {
        this.addListen(RenderTooltipEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderTooltipEvent.class);
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

    private String getShulkerName(ItemStack stack) {
        String name = stack.getName().asString();

        return name;
    }

    private void renderBackground(MatrixStack matrices, ItemStack stack, int x, int y) {
        RenderSystem.setShaderTexture(0, this.BACKGROUND_TEXTURE);

        DrawableHelper.drawTexture(matrices, x, y, 0, 0, 176, 78, 176, 78);
        TextRenderer r = CheatClient.getClient().textRenderer;

        // r.draw(matrices, stack.getName(), x + 7, y - 4, 0xFF404040);
        r.draw(matrices, stack.getName(), x + 7, y + 6, 0xFF404040);
    }

    private void renderTooltip(MatrixStack mStack, ItemStack stack, int x, int y) {
        List<ItemStack> items = this.getItems(stack);
        ItemRenderer ir = CheatClient.getClient().getItemRenderer();
        TextRenderer r = CheatClient.getClient().textRenderer;

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
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderTooltipEvent": {
                RenderTooltipEvent e = (RenderTooltipEvent)event;
                Item item = e.stack.getItem();
                
                // Make sure that it was a block
                if (!(item instanceof BlockItem)) break;

                // Make sure it is a shulker
                Block block = ((BlockItem)item).getBlock();
                if (!(block instanceof ShulkerBoxBlock)) break;

                // Cancel the old tool tip
                e.ci.cancel();

                // Offset where we are going to render the tooltip
                int x = e.x + 8;
                int y = e.y - 16;

                // Render the tool tip
                this.renderTooltip(e.mStack, e.stack, x, y);

                break;
            }
        }
    }
}
