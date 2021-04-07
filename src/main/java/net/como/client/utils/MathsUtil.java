package net.como.client.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MathsUtil {
    public static Vec3d getForwardVelocity(Entity entity) {
        return getVelocityRelYaw(yawInRadians(entity));
    }

    public static Vec3d getRightVelocity(Entity entity) {
        // Just forward but the angle is pi/2 radians greater (or for people who don't speak radians 90 deg) which is adjacent to line of forward travel.
        Double yaw = yawInRadians(entity) + Math.PI/2;

        return getVelocityRelYaw(yaw);
    }

    public static Double yawInRadians(Entity entity) {
        // All of the trig functions use radians so we must convert to this.
        return Math.toRadians(entity.yaw);
    }

    public static Vec3d getVelocityRelYaw(Double yaw) {
        return new Vec3d(-Math.sin(yaw), 0, Math.cos(yaw));
    }
}
