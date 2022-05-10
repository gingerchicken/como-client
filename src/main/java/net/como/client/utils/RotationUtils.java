package net.como.client.utils;

import net.como.client.ComoClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtils {
    public static Vec3d getEyePos() {
        ClientPlayerEntity me = ComoClient.me();

        return new Vec3d(
            me.getX(),
            me.getY() + me.getEyeHeight(me.getPose()),
            me.getZ()
        );
    }
    
	public static Vec3d getClientLookVec() {
		ClientPlayerEntity player = ComoClient.me();
		return getVec(player.getYaw(), player.getPitch());
	}

    public static Vec3d getVec(float yaw, float pitch) {
        yaw   = (float)Math.toRadians(yaw);
        pitch = (float)Math.toRadians(pitch);
		
		float f1 = -MathHelper.cos(yaw);
		float f2 = MathHelper.sin(yaw);

		float f3 = -MathHelper.cos(pitch);
		float f4 = MathHelper.sin(-pitch);

        return new Vec3d(f2 * f3, f4, f1 * f3);
    }

    public static Rotation getRequiredRotation(Vec3d vec, float tickDelta) {
        Vec3d offset = getEyePos().subtract(ComoClient.me().getPos());
        Vec3d eyesPos = ComoClient.me().getLerpedPos(tickDelta).add(offset);
		
        Vec3d delta = MathsUtils.Vec3dDiff(vec, eyesPos);
		
		double diffXZ = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
		
		double yaw = Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90d;
		double pitch = -Math.toDegrees(Math.atan2(delta.y, diffXZ));
		
		return new Rotation(yaw, pitch);
	}

    public static Rotation getRequiredRotation(Vec3d vec) {
        return getRequiredRotation(vec, 0);
    }

    public static final class Rotation {
        public final double yaw;
        public final double pitch;

        public Rotation(double yaw, double pitch) {
            this.yaw = MathHelper.wrapDegrees(yaw);
            this.pitch = MathHelper.wrapDegrees(pitch);
        }

        public double magnitude() {
            return Math.sqrt(this.yaw*this.yaw + this.pitch*this.pitch);
        }

        public Rotation difference(Rotation other) {
            return new Rotation(this.yaw - other.yaw, this.pitch - other.pitch);
        }
    }
}
