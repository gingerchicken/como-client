package net.como.client.cheats;

import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.como.client.utils.BlockUtils;

public class XRay extends Cheat {
    private Boolean fullbrightWasEnabled;

    public XRay() {
        super("XRay");

        this.addSetting(new Setting("AutoFullbright", true));

        this.addSetting(new Setting("DesiredBlocks", new HashMap<String, Boolean>()));

        // Non-specific search (more lag expected)
        this.addSetting(new Setting("NonSpecificSearch", false));
        this.addSetting(new Setting("BlockSearch", new HashMap<String, Boolean>()));

        this.description = "See blocks through the floor.";
    }

    private boolean shouldOverrideFullbright() {
        return ((boolean)this.getSetting("AutoFullbright").value && !this.fullbrightWasEnabled);
    }

    @Override
    public void activate() {
        MinecraftClient client = CheatClient.getClient();
        client.worldRenderer.reload();

        this.fullbrightWasEnabled = CheatClient.Cheats.get("fullbright").isEnabled();

        if (this.shouldOverrideFullbright()) CheatClient.Cheats.get("fullbright").enable(); 
    }

    @Override
    public void deactivate() {
        MinecraftClient client = CheatClient.getClient();
        client.worldRenderer.reload();

        if (this.shouldOverrideFullbright()) CheatClient.Cheats.get("fullbright").disable();
        
        this.fullbrightWasEnabled = null;
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            // For stuff such as rendering chests etc.
            case "onBlockEntityRender": {
                // Get the arguments
                BlockEntity blockEntity = (BlockEntity)args[0];
                CallbackInfo ci = (CallbackInfo)args[4];

                // Get the block id.
                String blockId = BlockUtils.getName(blockEntity.getPos());

                // Check if it is not a block we want to see and not display it if that is the case.
                if (!isDesiredBlock(blockId)) ci.cancel();

                break;
            }

            case "onBlockCracksRender": {
                // Disable the cracks etc. in the blocks when we mine them
                BlockPos blockPos = (BlockPos)args[3];
                CallbackInfoReturnable<Boolean> cir = (CallbackInfoReturnable<Boolean>)args[10];

                String blockId = BlockUtils.getName(blockPos);

                if (!isDesiredBlock(blockId)) cir.cancel();

                break;
            }

            case "onShouldDrawBlockSide": {
                BlockPos blockPos = (BlockPos)args[2];
                CallbackInfoReturnable<Boolean> cir = (CallbackInfoReturnable<Boolean>)args[5];

                String blockId = BlockUtils.getName(blockPos);

                cir.setReturnValue(isDesiredBlock(blockId));

                break;
            }

            // Make sure that the blocks are light underground
            case "onGetAmbientOcclusionLightLevel": {
                CallbackInfoReturnable<Float> cir = (CallbackInfoReturnable<Float>)args[2];

                cir.setReturnValue(1f);

                break;
            }

            // TODO hide liquids
            // TODO render all ores even if they normally are not rendered.
        }
    }
}
