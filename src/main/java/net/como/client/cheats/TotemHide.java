package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class TotemHide extends Cheat {
    boolean smallTotem = false;

    public TotemHide() {
        super("Totem Hide");
    }
    
    @Override
    public void recieveEvent(String eventName, Object[] args) {
        if (!this.isEnabled()) return;

        switch (eventName) {
            case "onRenderItem": {
                // Get the entity arguemnt
                LivingEntity entity                     = (LivingEntity)args[0];

                // We want to see it if there are other players.
                if (entity != CheatClient.me()) break;

                // The other arguments
                ItemStack stack                         = (ItemStack)args[1];
                ModelTransformation.Mode renderMode     = (ModelTransformation.Mode)args[2];
                // boolean leftHanded                      = (boolean)args[3];
                MatrixStack matrices                    = (MatrixStack)args[4];
                // VertexConsumerProvider vertexConsumers  = (VertexConsumerProvider)args[5];
                // int light                               = (int)args[6];
                CallbackInfo ci                         = (CallbackInfo)args[7];

                // Only do it if it is in the lefthand first person
                if (renderMode == ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND) {
                    // Make sure that it is the totem
                    if (stack.getItem().toString() == "totem_of_undying") {
                        if (this.smallTotem) {
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