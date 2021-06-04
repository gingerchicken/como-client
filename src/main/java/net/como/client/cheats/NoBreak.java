package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;

public class NoBreak extends Cheat {
    public NoBreak() {
        super("NoBreak");

        // TODO maybe add mending only setting?
        this.settings.addSetting(new Setting("MinDurability", 1));

        this.description = "Prevent your pickaxes from accidentally breaking.";
    }

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onSendPacket": {
                Packet<?> packet = (Packet<?>)args[0];
                CallbackInfo ci = (CallbackInfo)args[1];

                if (packet instanceof PlayerActionC2SPacket) {
                    PlayerActionC2SPacket actionPacket = (PlayerActionC2SPacket)packet;
                    
                    switch (actionPacket.getAction()) {
                        // Stop destroy block means that it stops destroying i.e. it breaks the block
                        case STOP_DESTROY_BLOCK: {
                            // Get the current item in the player's hand
                            ItemStack usedItem = CheatClient.me().getMainHandStack();

                            // Ignore if it is not a mining tool.
                            if (!(usedItem.getItem() instanceof net.minecraft.item.MiningToolItem)) break;

                            // Get the current durability of the item.
                            Integer currentDurability = usedItem.getMaxDamage() - usedItem.getDamage();

                            // Get the minimum durability
                            Integer minDurability = (Integer)this.settings.getSetting("MinDurability").value;

                            // If the item is still sufficiently durable still break the block.
                            if (currentDurability > minDurability) break;

                            // Make sure to tell the user what we are doing.
                            CheatClient.displayChatMessage(String.format("Breaking prevented by NoBreak (less than %d durability met on active tool.)", minDurability));

                            // We just need to tell the server that we actually stopped mining the block
                            PlayerActionC2SPacket stopPacket = new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, actionPacket.getPos(), actionPacket.getDirection());
                            CheatClient.me().networkHandler.sendPacket(stopPacket);

                            // Cancel the original packet that was to be sent.
                            ci.cancel();
                            break;
                        }

                        default: break;
                    }
                }

                break;
            }
        }
    }
}
