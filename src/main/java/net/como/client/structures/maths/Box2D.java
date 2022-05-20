package net.como.client.structures.maths;

import net.como.client.components.ProjectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

public class Box2D {
    public final Vec3 min;
    public final Vec3 max;

    public Box2D(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the center of the box
     * @return center position
     */
    public Vec3 getCenter() {
        return new Vec3(
            min.x + (max.x - min.x) / 2,
            min.y + (max.y - min.y) / 2,
            min.z + (max.z - min.z) / 2
        );
    }

    public static Box2D fromEntity(Entity entity, float tickDelta) {
        Vec3 bottom = new Vec3();
        Vec3 top = new Vec3();

        Box box = entity.getBoundingBox();

        double x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX()) - entity.getX();
        double y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY()) - entity.getY();
        double z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ()) - entity.getZ();

        // Check corners
        bottom.set(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        top.set(0, 0, 0);

        //     Bottom
        if (checkCorner(box.minX + x, box.minY + y, box.minZ + z, bottom, top)) return null;
        if (checkCorner(box.maxX + x, box.minY + y, box.minZ + z, bottom, top)) return null;
        if (checkCorner(box.minX + x, box.minY + y, box.maxZ + z, bottom, top)) return null;
        if (checkCorner(box.maxX + x, box.minY + y, box.maxZ + z, bottom, top)) return null;

        //     Top
        if (checkCorner(box.minX + x, box.maxY + y, box.minZ + z, bottom, top)) return null;
        if (checkCorner(box.maxX + x, box.maxY + y, box.minZ + z, bottom, top)) return null;
        if (checkCorner(box.minX + x, box.maxY + y, box.maxZ + z, bottom, top)) return null;
        if (checkCorner(box.maxX + x, box.maxY + y, box.maxZ + z, bottom, top)) return null;

        return new Box2D(bottom, top);
    }

    private static boolean checkCorner(double x, double y, double z, Vec3 min, Vec3 max) {
        Vec3 pos = new Vec3(x, y, z);
        if (!ProjectionUtils.to2D(pos, 1)) return true;

        // Check Min
        if (pos.x < min.x) min.x = pos.x;
        if (pos.y < min.y) min.y = pos.y;
        if (pos.z < min.z) min.z = pos.z;

        // Check Max
        if (pos.x > max.x) max.x = pos.x;
        if (pos.y > max.y) max.y = pos.y;
        if (pos.z > max.z) max.z = pos.z;

        return false;
    }
}
