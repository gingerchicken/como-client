package net.como.client.interfaces.mixin;

import net.minecraft.client.network.PendingUpdateManager;

public interface IClientWorld {
    public PendingUpdateManager obtainPendingUpdateManager();
}
