package net.como.client.interfaces.mixin;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;

public interface IMatrix4f {
    public Vector4f multiplyMatrix(Vector4f v);
    public Vec3d multiply3dMatrix(Vec3d v);
}
