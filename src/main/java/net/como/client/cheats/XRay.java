package net.como.client.cheats;

import java.util.HashMap;

import net.como.client.CheatClient;
import net.como.client.events.BlockCracksRenderEvent;
import net.como.client.events.BlockEntityRenderEvent;
import net.como.client.events.GetAmbientOcclusionLightLevelEvent;
import net.como.client.events.ShouldDrawBlockSideEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.como.client.utils.BlockUtils;

@SuppressWarnings("unchecked")
public class XRay extends Cheat {
    private Boolean fullbrightWasEnabled;

    public XRay() {
        super("XRay");
        this.description = "See blocks through the floor.";

        this.addSetting(new Setting("AutoFullbright", true));
        this.addSetting(new Setting("DesiredBlocks", new HashMap<String, Boolean>()));
        // Non-specific search (more lag expected)
        this.addSetting(new Setting("NonSpecificSearch", false));
        this.addSetting(new Setting("BlockSearch", new HashMap<String, Boolean>()));
    }

    private boolean shouldOverrideFullbright() {
        return ((boolean)this.getSetting("AutoFullbright").value && !this.fullbrightWasEnabled);
    }
    private Boolean isDesiredBlock(String blockId) {
        boolean isDesired = (((HashMap<String, Boolean>)this.getSetting("DesiredBlocks").value).containsKey(blockId));
        
        if (!isDesired && (boolean)this.getSetting("NonSpecificSearch").value) {
            HashMap<String, Boolean> blocksToSearch = (HashMap<String, Boolean>)this.getSetting("BlockSearch").value;
            
            for (String phrase : blocksToSearch.keySet()) {
                if (!blocksToSearch.get(phrase)) continue;

                if (blockId.contains(phrase)) {
                    isDesired = true;
                    break;
                }
            }
        }

        return isDesired;
    }


    @Override
    public void activate() {
        MinecraftClient client = CheatClient.getClient();
        client.worldRenderer.reload();

        this.fullbrightWasEnabled = CheatClient.Cheats.get("fullbright").isEnabled();

        if (this.shouldOverrideFullbright()) CheatClient.Cheats.get("fullbright").enable();

        this.addListen(BlockEntityRenderEvent.class);
        this.addListen(BlockCracksRenderEvent.class);
        this.addListen(ShouldDrawBlockSideEvent.class);
        this.addListen(GetAmbientOcclusionLightLevelEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(BlockEntityRenderEvent.class);
        this.removeListen(BlockCracksRenderEvent.class);
        this.removeListen(ShouldDrawBlockSideEvent.class);
        this.removeListen(GetAmbientOcclusionLightLevelEvent.class);

        MinecraftClient client = CheatClient.getClient();
        client.worldRenderer.reload();

        if (this.shouldOverrideFullbright()) CheatClient.Cheats.get("fullbright").disable();
        
        this.fullbrightWasEnabled = null;
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "BlockEntityRenderEvent": {
                BlockEntityRenderEvent e = (BlockEntityRenderEvent)event;

                // Get the block id.
                String blockId = BlockUtils.getName(e.blockEntity.getPos());

                // Check if it is not a block we want to see and not display it if that is the case.
                if (!isDesiredBlock(blockId)) e.ci.cancel();

                break;
            }
            case "BlockCracksRenderEvent": {
                BlockCracksRenderEvent e = (BlockCracksRenderEvent)event;

                // Get the block at the position
                String blockId = BlockUtils.getName(e.pos);

                // If it is not what we want, don't render the cracks
                if (!isDesiredBlock(blockId)) e.cir.cancel();

                break;
            }
            case "ShouldDrawBlockSideEvent": {
                ShouldDrawBlockSideEvent e = (ShouldDrawBlockSideEvent)event;

                // Get the blockId as a String
                String blockId = BlockUtils.getName(e.pos);

                // Say if we want it or not.
                e.cir.setReturnValue(isDesiredBlock(blockId));

                break;
            }
            case "GetAmbientOcclusionLightLevelEvent": {
                ((GetAmbientOcclusionLightLevelEvent)(event)).cir.setReturnValue(1f);
                break;
            }
        }
    }
}
