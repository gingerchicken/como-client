package net.como.client.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak");

        this.description = "Allows you to break blocks a bit quicker.";

        this.addSetting(new Setting("PotionAmplifier", 3));
        this.addSetting(new Setting("Potion", true));
        this.addSetting(new Setting("BreakDelay", 0d));
    }

    @Override
    public String listOption() {
        return this.getBoolSetting("Potion") ? "Potion" : "Packet";
    }

    public void resetPotionEffect() {
        if (ComoClient.me().hasStatusEffect(StatusEffects.HASTE)) {
            ComoClient.me().removeStatusEffect(StatusEffects.HASTE);
        }
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(SendPacketEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(SendPacketEvent.class);

        this.resetPotionEffect();
        this.targetBlocks.clear();
    }

    private static class TimedBreak {
        private BlockPos block;
        public Double breakTime;
        private Direction direction;

        public BlockPos getBlockPos() {return this.block;};

        public boolean doBreak() {
            if (ComoClient.getCurrentTime() < this.breakTime) return false;

            ComoClient.me().networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, this.block, direction));

            return true;
        }

        TimedBreak(BlockPos pos, Direction direction, Double delay) {
            this.block = pos;
            this.direction = direction;

            this.breakTime = ComoClient.getCurrentTime() + delay;
        }
    }

    private Boolean addBlock(PlayerActionC2SPacket packet) {
        if (this.targetBlocks.containsKey(packet.getPos())) return false;

        this.targetBlocks.put(packet.getPos(), new TimedBreak(packet.getPos(), packet.getDirection(), this.getDoubleSetting("BreakDelay")));
        return true;
    }

    private Boolean removeBlock(BlockPos pos) {
        if (!this.targetBlocks.containsKey(pos)) return false;

        this.targetBlocks.remove(pos);

        return true;
    }

    private HashMap<BlockPos, TimedBreak> targetBlocks = new HashMap<>(); 

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                // Handle simple potion effect.
                if (this.getBoolSetting("Potion")) {
                    ComoClient.me().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 3, this.getIntSetting("PotionAmplifier"), true, true));

                    break;
                }
                
                // Handle packet
                this.resetPotionEffect();

                List<BlockPos> removedPositions = new ArrayList<BlockPos>();

                // Do all the block breaking
                for (TimedBreak timedBreak : targetBlocks.values()) {
                    if (timedBreak.doBreak()) {
                        removedPositions.add(timedBreak.getBlockPos());
                    }
                }

                // Remove the blocks from the array.
                for (BlockPos pos : removedPositions) {
                    this.removeBlock(pos);
                }
                break;
            }

            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;

                if (!(e.packet instanceof PlayerActionC2SPacket)) break;

                PlayerActionC2SPacket packet = (PlayerActionC2SPacket)e.packet;

                // Handle mining a block
                switch (packet.getAction()) {
                    case ABORT_DESTROY_BLOCK:
                    case STOP_DESTROY_BLOCK: {
                        this.removeBlock(packet.getPos());
                        break;
                    }

                    case START_DESTROY_BLOCK: {
                        this.addBlock(packet);

                        break;
                    }

                    default: {
                        break;
                    }
                }
            }
        }
    }
}
