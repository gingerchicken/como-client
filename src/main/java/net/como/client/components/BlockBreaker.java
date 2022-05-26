package net.como.client.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.packet.SendPacketEvent;
import net.como.client.interfaces.ModulePlugin;
import net.como.client.misc.Module;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.MathsUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BlockBreaker implements ModulePlugin {
    private ServerClientRotation scr = new ServerClientRotation();
    private List<BlockPos> blocks = new ArrayList<>();

    public boolean isBreakingBlock(BlockPos pos) {
        return this.blocks.contains(pos);
    }

    public boolean startBreakBlock(BlockPos pos) {
        if (blocks.contains(pos)) return false;

        blocks.add(pos);

        return true;
    }

    public boolean stopBreakBlock(BlockPos pos) {
        if (!this.isBreakingBlock(pos)) return false;

        this.blocks.remove(pos);
        if (this.started.contains(pos)) this.started.remove(pos);

        return true;
    }

    public void stopBreakAll() {
        this.blocks.clear();
        this.started.clear();
    }

    @Override
    public void addListeners(Module parentModule) {
        scr.addListeners(parentModule);

        parentModule.addListen(ClientTickEvent.class);
    }

    @Override
    public void removeListeners(Module parentModule) {
        scr.removeListeners(parentModule);
        
        parentModule.removeListen(ClientTickEvent.class);
    }

    public BlockPos getTargetBlock() {
        int i = this.blocks.size() - 1;

        if (i < 0) return null;

        return this.blocks.get(i);
    }

    private List<BlockPos> started = new ArrayList<>();
    private void sendStartBreakPacket(BlockPos pos) {
        if (started.contains(pos)) return;

        ComoClient.me().networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
        started.add(pos);
    }

    private boolean lookAtBlock(BlockPos pos) {
        if (!this.forceAngles) return false;

        // Get the centre of the block
        Vec3d centrePos = MathsUtils.blockPosToVec3d(pos);

        // Look at it
        if (this.clientAngles) this.scr.lookAtPosClient(centrePos);
        else this.scr.lookAtPosServer(centrePos);

        return true;
    }

    private boolean isValid(BlockPos pos) {
        return pos != null && !BlockUtils.getState(pos).isAir();
    }

    // Sends break packets to all of the surrounding blocks
    private void fastBreakAll() {
        for (BlockPos pos : this.blocks) {
            if (!this.isValid(pos)) continue;

            this.lookAtBlock(pos);

            this.sendStartBreakPacket(pos);
            this.sendBreakPacket(pos);
        }
    }

    private void sendBreakPacket(BlockPos pos) {
        ComoClient.me().networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.UP));
    }

    // Gets the last block in the stack and breaks it.
    private void breakNext() {
        // Get the back of the list
        BlockPos pos = this.getTargetBlock();
        
        if (!this.isValid(pos)) {
            this.stopBreakBlock(pos);
            return;
        }

        this.lookAtBlock(pos);

        if (this.forceBreak) {
            // Break it.
            this.sendStartBreakPacket(pos);
            this.sendBreakPacket(pos);

        } else {
            // Process the block breaking.
            ComoClient.getClient().interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
        }
    }

    @Override
    public boolean fireEvent(Event event) {
        if (scr.fireEvent(event)) return true;

        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (this.breakInSeries) {
                    this.breakNext();

                    return false;
                }

                if (this.forceBreak) {
                    this.fastBreakAll();

                    return false;
                }

                // Invalid state

                return false;
            }
        }

        // I will return false since I guess you might want to also do other things that tick too.
        return false;
    }
    
    public boolean forceAngles = false;
    public boolean forceBreak = true;
    public boolean breakInSeries = false;
    public boolean clientAngles = false;

    public Boolean isInvalidState() {
        return (!this.breakInSeries && !this.forceBreak);
    }
}
