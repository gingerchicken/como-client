package net.como.client.interfaces.mixin;

import net.minecraft.util.math.Vec3d;

public interface ICamera {
    public void setPitch(float pitch);
    public void setYaw(float yaw);

    public void forceSetPos(Vec3d pos);
    public default void forceSetPos(float x, float y, float z) {
        this.forceSetPos(new Vec3d(x, y, z));
    }
}
