package net.como.client.modules.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.client.UpdateBlockBreakingProgressEvent;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.misc.Mode;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak");

        this.setDescription("Allows you to break blocks a bit quicker.");

        this.addSetting(new Setting("Mode", new Mode("Multiplier", "Packet", "Potion")));
        this.addSetting(new Setting("Multiplier", 3));
        this.addSetting(new Setting("BreakDelay", 0d));

        this.setCategory("Packet");
    }

    @Override
    public String listOption() {
        return String.format("%s x%d", this.getModeSetting("Mode").getStateName(), this.getIntSetting("Multiplier"));
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
        this.addListen(UpdateBlockBreakingProgressEvent.class);

        this.ignoreRequests = 0;
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(SendPacketEvent.class);
        this.removeListen(UpdateBlockBreakingProgressEvent.class);

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

    private boolean handlePotions() {
        ComoClient.me().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 3, this.getIntSetting("Multiplier"), true, true));
        return true;
    }

    private boolean handlePackets() {
        // Remove the potion effect since we are no longer in that mode.
        this.resetPotionEffect();

        // Positions that we need to remove from the hashmap (we cannot remove during iteration over the hashmap.)
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

        return true;
    }

    private HashMap<BlockPos, TimedBreak> targetBlocks = new HashMap<>(); 
    private Integer ignoreRequests = 0;

    @Override
    public void fireEvent(Event event) {
        Mode mode = this.getModeSetting("Mode");
        Integer multiplier = this.getIntSetting("Multiplier");

        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                switch (mode.getStateName()) {
                    case "Multiplier": {
                        this.resetPotionEffect();
                        break;
                    }
                    case "Potion": {
                        this.handlePotions();
                        break;
                    }
                    case "Packet": {
                        this.handlePackets();
                        break;
                    }
                }

                break;
            }

            case "SendPacketEvent": {
                if (!mode.is("Packet")) break;

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

                break;
            }

            case "UpdateBlockBreakingProgressEvent": {
                if (!mode.is("Multiplier")) break;

                UpdateBlockBreakingProgressEvent e = (UpdateBlockBreakingProgressEvent)event;
                
                if (multiplier <= 1) break; // Handle if there is no change. 

                // Don't block anything that we spawned.
                if (ignoreRequests > 0) {
                    ignoreRequests--;
                    break;
                }

                // Cancel the current call.
                e.cir.cancel();

                // Add the newly called functions
                this.ignoreRequests += multiplier;

                // Call them
                for (int i = 0; i < multiplier; i++) {
                    ComoClient.getClient().interactionManager.updateBlockBreakingProgress(e.pos, e.direction);
                }
                
                break;
            }
        }
    }
}
