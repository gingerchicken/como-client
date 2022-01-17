package net.como.client.modules;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.components.BlockBreaker;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.RenderWorldEvent;
import net.como.client.structures.Colour;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Nuker extends Module {

    private BlockBreaker breaker = new BlockBreaker();

    public Nuker() {
        super("Nuker");
        this.description = "Currently in development so this doesn't do anything yet!";

        this.addSetting(new Setting("Radius", 2));

        this.addSetting(new Setting("SeriesBreak", true));
        this.addSetting(new Setting("ForceBreak",  false));
        this.addSetting(new Setting("ForceAngles", false));
        this.addSetting(new Setting("Silent", true));
    }
    
    private void resetBlocks() {
        this.breaker.stopBreakAll();
        this.blocks.clear();
    }

    private List<BlockPos> targetBlocks() {
        Integer radius = this.getIntSetting("Radius");

        BlockPos from   = ComoClient.me().getBlockPos().add(radius, radius, radius);
        BlockPos to     = ComoClient.me().getBlockPos().add(-radius, -radius, -radius);

        List<BlockPos> selectBlocks = new ArrayList<>();
		
		BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
		BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
		
        Vec3d playerPos = ComoClient.me().getPos();

		// Oh boy, O(n^3) :D
		for(int x = min.getX(); x <= max.getX(); x++) {
			for(int y = min.getY(); y <= max.getY(); y++) {
				for(int z = min.getZ(); z <= max.getZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    
                    BlockState state = BlockUtils.getState(pos);

                    // Make sure the block isn't air
                    if (state.isAir()) continue;

                    // Make sure that the block's centre isn't too far away.
                    if (MathsUtils.blockPosToVec3d(pos).distanceTo(playerPos) > radius) continue;

                    // TODO add something a that only selects the wanted blocks by the player.

                    selectBlocks.add(pos);
                }
            }
        }

        return selectBlocks;
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(RenderWorldEvent.class);

        breaker.addListeners(this);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(RenderWorldEvent.class);
        
        breaker.removeListeners(this);

        this.breaker.stopBreakAll();
        this.blocks.clear();
        this.lastScannedPos = BlockPos.ORIGIN;
    }

    private BlockPos lastScannedPos = BlockPos.ORIGIN;

    private boolean hasPositionChanged() {
        BlockPos curPos = ComoClient.me().getBlockPos();

        // You could check if the distance is zero but that requires processor intensive maths to be done.
        return !(
            lastScannedPos.getX() == curPos.getX()
            && lastScannedPos.getY() == curPos.getY()
            && lastScannedPos.getZ() == curPos.getZ()
        );
    }

    private List<BlockPos> blocks = new ArrayList<>();

    private void loadBlocks() {
        this.resetBlocks();

        this.blocks = this.targetBlocks();

        // Add all breakers
        for (BlockPos pos : this.blocks) {
            breaker.startBreakBlock(pos);
        }
    }

    private void syncSettings() {
        this.breaker.breakInSeries = this.getBoolSetting("SeriesBreak");
        this.breaker.forceBreak    = this.getBoolSetting("ForceBreak");
        this.breaker.forceAngles   = this.getBoolSetting("ForceAngles");
        this.breaker.clientAngles  = !this.getBoolSetting("Silent");
    }

    @Override
    public void fireEvent(Event event) {
        if (this.breaker.fireEvent(event)) return;

        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                this.syncSettings();

                if (!this.hasPositionChanged()) break;

                this.lastScannedPos = ComoClient.me().getBlockPos();

                // Update our block list.
                this.loadBlocks();
                
                break;
            }

            case "RenderWorldEvent": {
                RenderWorldEvent e = (RenderWorldEvent)event;

                for (BlockPos pos : this.blocks) {
                    RenderUtils.renderBlockBox(e.mStack, pos);
                }
                
                break;
            }
        }
    }
}
