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
    private Double normalGamma;

    public XRay() {
        super("XRay");

        settings.addSetting(new Setting("DesiredBlocks", new HashMap<String, Boolean>()));
        this.description = "See ores through the floor.";
    }

    @Override
    public void activate() {
        MinecraftClient client = CheatClient.getClient();
        client.worldRenderer.reload();

        this.normalGamma = client.options.gamma;
        client.options.gamma = 16;
    }

    @Override
    public void deactivate() {
        MinecraftClient client = CheatClient.getClient();
        client.worldRenderer.reload();

        // Restore the user's setting
        this.normalGamma = client.options.gamma;
        client.options.gamma = this.normalGamma;
    }

    private Boolean isDesiredBlock(String blockId) {
        return (((HashMap<String, Boolean>)this.settings.getSetting("DesiredBlocks").value).containsKey(blockId));
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
                CallbackInfoReturnable<Boolean> cir = (CallbackInfoReturnable<Boolean>)args[4];

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
