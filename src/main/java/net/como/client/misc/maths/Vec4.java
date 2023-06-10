/**
 * Based from the Meteor Client 3D vector, thank you!
 */

package net.como.client.misc.maths;

import org.joml.Vector4d;

public class Vec4 {
    public double x, y, z, w;

    public void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void toScreen() {
        double newW = 1.0d / w * 0.5d;

        x = x * newW + 0.5d;
        y = y * newW + 0.5d;
        z = z * newW + 0.5d;

        w = newW;
    }

    public Vector4d toVec4d() {
        return new Vector4d(x, y, z, w);
    }
}
