package net.como.client.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.como.client.interfaces.mixin.IMatrix4f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;

@Mixin(Matrix4f.class)
public class Matrix4fMixin implements IMatrix4f {
    @Shadow protected float a00;
    @Shadow protected float a10;
    @Shadow protected float a20;
    @Shadow protected float a30;

    @Shadow protected float a01;
    @Shadow protected float a11;
    @Shadow protected float a21;
    @Shadow protected float a31;

    @Shadow protected float a02;
    @Shadow protected float a12;
    @Shadow protected float a22;
    @Shadow protected float a32;

    @Shadow protected float a03;
    @Shadow protected float a13;
    @Shadow protected float a23;
    @Shadow protected float a33;

    @Override
    public Vector4f multiplyMatrix(Vector4f v) {
        return new Vector4f(
            a00 * v.getX() + a01 * v.getY() + a02 * v.getZ() + a03 * v.getW(),
            a10 * v.getX() + a11 * v.getY() + a12 * v.getZ() + a13 * v.getW(),
            a20 * v.getX() + a21 * v.getY() + a22 * v.getZ() + a23 * v.getW(),
            a30 * v.getX() + a31 * v.getY() + a32 * v.getZ() + a33 * v.getW()
        );
    }

    @Override
    public Vec3d multiply3dMatrix(Vec3d v) {
        return new Vec3d(
            v.x * a00 + v.y * a01 + v.z * a02 + a03,
            v.x * a10 + v.y * a11 + v.z * a12 + a13,
            v.x * a20 + v.y * a21 + v.z * a22 + a23
        );
    }
    
}
