package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.structures.events.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class GetSkinTextureEvent extends Event {
    public PlayerEntity player;
    public CallbackInfoReturnable<Identifier> cir;

    public GetSkinTextureEvent(PlayerEntity player, CallbackInfoReturnable<Identifier> cir) {
        this.player = player;
        this.cir = cir;
    }
}
