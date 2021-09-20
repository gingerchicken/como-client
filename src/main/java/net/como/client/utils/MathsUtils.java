package net.como.client.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MathsUtils {
    public static Vec3d getForwardVelocity(Entity entity) {
        return getVelocityRelYaw(yawInRadians(entity));
    }

    public static Vec3d getRightVelocity(Entity entity) {
        // Just forward but the angle is pi/2 radians greater (or for people who don't speak radians 90 deg) which is adjacent to line of forward travel.
        Double yaw = yawInRadians(entity) + Math.PI/2;

        return getVelocityRelYaw(yaw);
    }

    public static Vec3d getLerpedCentre(Entity ent, float delta) {
        Vec3d lerped = ent.getLerpedPos(delta);
        Vec3d centre = ent.getBoundingBox().getCenter();

        Vec3d diff = ent.getPos().add(
            centre.multiply(-1)   
        );

        return lerped.add(diff.multiply(-1));
    }

    public static Double yawInRadians(Entity entity) {
        // All of the trig functions use radians so we must convert to this.
        return Math.toRadians(entity.getYaw());
    }

    public static Double pitchInRadians(Entity entity) {
        return Math.toRadians(entity.getPitch());
    }

    public static Vec3d getVelocityRelYaw(Double yaw) {
        return new Vec3d(-Math.sin(yaw), 0, Math.cos(yaw));
    }

    public static Integer getSign(Integer i) {
        return i >= 0 ? 1 : -1;
    }

    public static Vec3d blockPosToVec3d(BlockPos bP) {
        return new Vec3d(bP.getX() + 0.5, bP.getY() + 0.5, bP.getZ() + 0.5);
    }

    public static Vec3d Vec3dDiff(Vec3d v1, Vec3d v2) {
        return v1.add( v2.multiply(-1) );
    }
}
