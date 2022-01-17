package net.como.client.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.interfaces.ModulePlugin;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
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

    @Override
    public boolean fireEvent(Event event) {
        if (scr.fireEvent(event)) return true;

        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                // Get the back of the list
                BlockPos pos = this.getTargetBlock();
                if (pos == null) return false;

                if (BlockUtils.getState(pos).isAir()) this.stopBreakBlock(pos);

                // Get the centre of the block
                Vec3d centrePos = MathsUtils.blockPosToVec3d(pos);

                // Look at it
                if (this.forceAngles) this.scr.lookAtPosServer(centrePos);
                
                if (this.forceBreak) {
                    // Break it.
                    this.sendStartBreakPacket(pos);
                    ComoClient.me().networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.UP));
                    this.stopBreakBlock(pos);

                } else {
                    // Process the block breaking.
                    ComoClient.getClient().interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                }
            
                break;
            }
        }

        // I will return false since I guess you might want to also do other things that tick too.
        return false;
    }
    
    public boolean forceAngles = false;
    public boolean forceBreak = true;
}
