package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.minecraft.entity.Entity;

public class AntiItemDrop extends Cheat {
    public AntiItemDrop() {
        super("NoItemRender");

        this.description = "Hide all dropped items so then your friends cannot kill your client repeatedly.";
    }

    public void receiveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderEntity": {
                Entity entity   = (Entity)args[0];
                CallbackInfo ci = (CallbackInfo)args[7];

                if (entity instanceof net.minecraft.entity.ItemEntity) ci.cancel();
            }
        }
    }
}
