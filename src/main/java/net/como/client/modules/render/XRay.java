package net.como.client.modules.render;

import java.util.HashMap;

import net.como.client.ComoClient;
import net.como.client.events.BlockCracksRenderEvent;
import net.como.client.events.BlockEntityRenderEvent;
import net.como.client.events.GetAmbientOcclusionLightLevelEvent;
import net.como.client.events.ShouldDrawBlockSideEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.como.client.utils.BlockUtils;

@SuppressWarnings("unchecked")
public class XRay extends Module {
    private Boolean fullbrightWasEnabled;

    public XRay() {
        super("XRay");
        this.setDescription("See blocks through the floor.");

        this.addSetting(new Setting("AutoFullbright", true));
        this.addSetting(new Setting("DesiredBlocks", new HashMap<String, Boolean>()));
        // Non-specific search (more lag expected)
        this.addSetting(new Setting("NonSpecificSearch", false));
        this.addSetting(new Setting("BlockSearch", new HashMap<String, Boolean>()));

        this.setCategory("Render");
    }

    private boolean shouldOverrideFullbright() {
        return (this.getBoolSetting("AutoFullbright") && this.fullbrightWasEnabled != null && !this.fullbrightWasEnabled);
    }
    
    private Boolean isDesiredBlock(String blockId) {
        boolean isDesired = this.getHashMapSetting("DesiredBlocks").containsKey(blockId);

        if (!isDesired && this.getBoolSetting("NonSpecificSearch")) {
            HashMap<String, Boolean> blocksToSearch = this.getHashMapSetting("BlockSearch");
            
            for (String phrase : blocksToSearch.keySet()) {
                if (!blocksToSearch.containsKey(phrase) || !blocksToSearch.get(phrase)) continue;

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
        MinecraftClient client = ComoClient.getClient();
        client.worldRenderer.reload();

        this.fullbrightWasEnabled = ComoClient.Modules.get("fullbright").isEnabled();

        if (this.shouldOverrideFullbright()) ComoClient.Modules.get("fullbright").enable();

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

        MinecraftClient client = ComoClient.getClient();
        client.worldRenderer.reload();

        if (this.shouldOverrideFullbright()) ComoClient.Modules.get("fullbright").disable();
        
        this.fullbrightWasEnabled = false;
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
