package net.como.client.mixin.botch.craftingdupe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.ComoClient;
import net.como.client.modules.dupes.CraftingDupe;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    private CraftingDupe getCraftingDupe() {
        return (CraftingDupe)ComoClient.Modules.get("craftingdupe");
    }

    private Slot getTargetSlot() {
        return this.handler.slots.get(0);
    }

    @Inject(method = {"init"}, at = {@At("TAIL")})
    protected void init(CallbackInfo ci) {
        // Make sure the mod is enabled.
        if (!this.getCraftingDupe().isEnabled()) return;

        addDrawableChild(new ButtonWidget(this.x + 130, this.height / 2 - 24, 40, 20, Text.of("Dupe"), b -> performDupe()));
    }
    
    private void performDupe() {
        // Shout out to the NUMA developers, I think they found this dupe... Could be wrong though...
        Slot slot = this.getTargetSlot();
        this.onMouseClick(slot, slot.id, 0, SlotActionType.THROW);
    }
}
