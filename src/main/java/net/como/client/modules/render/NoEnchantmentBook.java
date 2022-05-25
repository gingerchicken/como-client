package net.como.client.modules.render;

import net.como.client.events.Event;
import net.como.client.events.render.BlockEntityRenderEvent;
import net.como.client.misc.Module;

public class NoEnchantmentBook extends Module {
    public NoEnchantmentBook() {
        super("NoEnchantBook");

        this.setDescription("Hide the enchantment book on the enchantment table.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(BlockEntityRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(BlockEntityRenderEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "BlockEntityRenderEvent": {
                BlockEntityRenderEvent e = (BlockEntityRenderEvent)event;

                // Don't render the block entity i.e. the book.
                if (e.blockEntity instanceof net.minecraft.block.entity.EnchantingTableBlockEntity) {
                    e.ci.cancel();
                }

                break;
            }
        }
    }
}
