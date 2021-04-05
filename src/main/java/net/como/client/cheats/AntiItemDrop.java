package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class AntiItemDrop extends Cheat {
    public AntiItemDrop() {
        super("Anti-Dropped Item Renderer");
    }

    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderEntity": {
                Entity entity   = (Entity)args[0];

                // double cameraX  = (double)args[1];
                // double cameraY  = (double)args[2];
                // double cameraZ  = (double)args[3];
                // float tickDelta = (float) args[4];
                
                // MatrixStack matrices = (MatrixStack)args[5];
                // VertexConsumerProvider vertexConsumers = (VertexConsumerProvider)args[6];
                
                CallbackInfo ci = (CallbackInfo)args[7];

                if (entity.getClass().getName() == "net.minecraft.entity.ItemEntity") ci.cancel();
                // System.out.println(50);
            }
        }
    }
}
