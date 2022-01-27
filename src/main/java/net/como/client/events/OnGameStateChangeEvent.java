package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;

public class OnGameStateChangeEvent extends Event {
    public GameStateChangeS2CPacket packet;
    public CallbackInfo ci;

    public OnGameStateChangeEvent(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        this.packet = packet;
        this.ci = ci;
    }
}
