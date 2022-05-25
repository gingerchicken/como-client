package net.como.client.modules.dupes;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.InteractionUtils;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ShulkerDupe extends Module {
    public ShulkerDupe() {
        super("ShulkerDupe");
        this.setDescription("Allows the user to duplicate shulker boxes (Vanilla Only.)");

        this.addSetting(new Setting("DupeAll", true));
        this.addSetting(new Setting("TargetSlot", 0));

        this.setCategory("Dupe");
    }

    @Override
    public String listOption() {
        if (this.getBoolSetting("DupeAll")) return "All";

        return String.format("Slot%d", this.getIntSetting("TargetSlot"));
    }

    public Boolean performDupe = false;

    private ItemStack getTargetSlotStack() {
        return ClientUtils.getHandlerSlot(this.getIntSetting("TargetSlot"));
    }

    public Text getDupeButtonText() {
        if (this.getBoolSetting("DupeAll")) return Text.of("Dupe All");

        ItemStack stack = this.getTargetSlotStack();
        String itemName = stack.getItem().toString().toUpperCase();
        Integer totalItems = stack.getCount();

        return Text.of(
            String.format("Dupe Slot %d (%s x%d)", this.getIntSetting("TargetSlot"), itemName, totalItems)
        );
    }

    public Boolean shouldActivateButton() {
        return !this.performDupe && (this.getBoolSetting("DupeAll") || this.getTargetSlotStack().getItem() != Items.AIR);
    }

    private void takeItems() {
		if (this.getBoolSetting("DupeAll")) {
            for (int i = 0; i < 27; i++) {
                InteractionUtils.fastPickupItem(i);
            }

            return;
        }

        InteractionUtils.fastPickupItem(this.getIntSetting("TargetSlot"));
	}

    @Override
    public void activate() {
        this.performDupe = false;

        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    public Boolean performBreak() {
        if (!this.performDupe) return false;

		// Make sure we're looking at a block
		HitResult hitResult = ComoClient.getClient().crosshairTarget;
		if (!(hitResult instanceof BlockHitResult)) return false;

		BlockHitResult blockHitResult = (BlockHitResult)hitResult;
		BlockPos blockPos = blockHitResult.getBlockPos();

		// Shulkers only
		if (!(ComoClient.getClient().world.getBlockState(blockPos).getBlock() instanceof ShulkerBoxBlock)) return false;
		if (!(ComoClient.getClient().player.currentScreenHandler instanceof ShulkerBoxScreenHandler)) return false;

		// Perform action
		ComoClient.getClient().interactionManager.updateBlockBreakingProgress(blockHitResult.getBlockPos(), Direction.DOWN);

		return true;
    }
    
    // I will create a hook for this manually since it has to be tick perfect.
    public void handlePacket(Packet<?> packet) {
            // Check if we should do anything.
            if (!this.performDupe) return;

            // Make sure that the packet is an action packet
            if (!(packet instanceof PlayerActionC2SPacket)) return;
            PlayerActionC2SPacket actionC2SPacket = (PlayerActionC2SPacket)packet;

            // Make sure that the item is a break block packet.
            if (actionC2SPacket.getAction() != PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) return;

            // Take out the items
            this.takeItems();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                this.performDupe = this.performBreak();

                break;
            }
        }
    }
}
