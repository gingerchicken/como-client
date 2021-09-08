package net.como.client.cheats;

import net.como.client.events.BlockEntityRenderEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;

public class NoEnchantmentBook extends Cheat {
    public NoEnchantmentBook() {
        super("NoEnchantBook");

        this.description = "Hide the enchantment book on the enchantment table.";
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
