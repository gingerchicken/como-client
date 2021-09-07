package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.minecraft.block.entity.BlockEntity;

public class NoEnchantmentBook extends Cheat {
    public NoEnchantmentBook() {
        super("NoEnchantBook");

        this.description = "Hide the enchantment book on the enchantment table.";
    }

    @Override
    public void receiveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onBlockEntityRender": {
                BlockEntity blockEntity = (BlockEntity)args[0];
                CallbackInfo ci = (CallbackInfo)args[4];

                // Don't render the block entity i.e. the book.
                if (blockEntity instanceof net.minecraft.block.entity.EnchantingTableBlockEntity) {
                    ci.cancel();
                }

                break;
            }
        }
    }
}
