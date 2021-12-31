package net.como.client.modules.utilities;

import net.como.client.ComoClient;
import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;

public class NoBreak extends Module {
    public NoBreak() {
        super("NoBreak");

        // TODO maybe add mending only setting?
        this.addSetting(new Setting("MinDurability", 1));

        this.description = "Prevent your pickaxes from accidentally breaking.";
    }

    @Override
    public void activate() {
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SendPacketEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;

                if (!(e.packet instanceof PlayerActionC2SPacket)) break;

                PlayerActionC2SPacket actionPacket = (PlayerActionC2SPacket)(e.packet);

                switch (actionPacket.getAction()) {
                    case STOP_DESTROY_BLOCK: {
                        // Get the current item in the player's hand
                        ItemStack usedItem = ComoClient.me().getMainHandStack();

                        // Ignore if it is not a mining tool.
                        if (!(usedItem.getItem() instanceof net.minecraft.item.MiningToolItem)) break;

                        // Get the current durability of the item.
                        Integer currentDurability = usedItem.getMaxDamage() - usedItem.getDamage();

                        // Get the minimum durability
                        Integer minDurability = (Integer)this.getSetting("MinDurability").value;

                        // If the item is still sufficiently durable still break the block.
                        if (currentDurability > minDurability) break;

                        // Make sure to tell the user what we are doing.
                        this.displayMessage(String.format("Breaking prevented by NoBreak (less than %d durability met on active tool.)", minDurability));

                        // We just need to tell the server that we actually stopped mining the block
                        PlayerActionC2SPacket stopPacket = new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, actionPacket.getPos(), actionPacket.getDirection());
                        ComoClient.me().networkHandler.sendPacket(stopPacket);

                        // Cancel the original packet that was to be sent.
                        e.ci.cancel();
                        break;
                    }
                    default: break;
                }
                break;
            }
        }
    }
}
