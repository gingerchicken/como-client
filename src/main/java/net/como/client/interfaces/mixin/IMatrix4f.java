package net.como.client.interfaces.mixin;

import net.como.client.misc.maths.Vec4;
import net.minecraft.util.math.Vec3d;

public interface IMatrix4f {
    public void multiplyMatrix(Vec4 vec4, Vec4 mmmat4);
    public Vec3d mul(Vec3d vec);
}
