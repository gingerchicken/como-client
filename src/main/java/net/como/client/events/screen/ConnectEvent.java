package net.como.client.events.screen;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.network.ServerInfo;

public class ConnectEvent extends Event {
    public ServerInfo entry;
    public CallbackInfo ci;
    
    public ConnectEvent(ServerInfo entry, CallbackInfo ci) {
        this.entry = entry;
        this.ci = ci;
    }
}
