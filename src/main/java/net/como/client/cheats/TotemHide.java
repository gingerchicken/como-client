package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class TotemHide extends Cheat {
    // boolean smallTotem = false;

    public TotemHide() {
        super("Totem Hide");

        settings.addSetting(new Setting<Boolean>("SmallMode", false));
    }
    
    @Override
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderItem": {
                // Get the entity arguemnt
                LivingEntity entity = (LivingEntity)args[0];

                // We want to see it if there are other players.
                if (entity != CheatClient.me()) break;

                // The other arguments
                ItemStack stack                      = (ItemStack)args[1];
                ModelTransformation.Mode renderMode  = (ModelTransformation.Mode)args[2];
                MatrixStack matrices                 = (MatrixStack)args[4];
                CallbackInfo ci                      = (CallbackInfo)args[7];

                // LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci

                // Only do it if it is in the lefthand first person
                if (renderMode == ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND) {
                    // Make sure that it is the totem
                    if (stack.getItem().toString() == "totem_of_undying") {
                        if ((boolean)this.settings.getSetting("SmallMode").value) {
                            // Render the small totem
                            matrices.scale(0.5f, 0.5f, 0.5f);
                            matrices.translate(-0.5f, 0.0f, 0.0f);
                            
                            // Stop
                            break;
                        }

                        // Else just don't render it.
                        ci.cancel();
                    }
                } else {
                    matrices.scale(1f, 1f, 1f);
                }
            }
        }
    }
}