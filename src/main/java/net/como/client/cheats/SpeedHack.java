package net.como.client.cheats;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.util.math.Vec3d;

public class SpeedHack extends Cheat {
    public SpeedHack() {
        super("Speed");
    }

    // Vec3d targetVelocity = new Vec3d(2, 1, 2);
    double acceleration = 1.1;
    double maxSpeed = 2;

    private double getSpeed(Vec3d vec) {
        return vec.multiply(1, 0, 1).distanceTo(new Vec3d(0, 0, 0));
    }

    private double getSign(double a) {
        if (a == 0) return 0;
        return a < 0 ? -1 : 1;
    }

    private Vec3d getNewVelocity() {
        Vec3d rotVector         = CheatClient.me().getRotationVector();
        Vec3d actualVelocity    = CheatClient.me().getVelocity();
        Vec3d fasterVelocity    = new Vec3d(0, 0, 0);

        // Cannot get anything nice to work for the life of me so...
        if (CheatClient.me().input.pressingLeft || CheatClient.me().input.pressingRight) {
            // I cannot be bothered to figure out how to do the left/right movements like the other method so.
            fasterVelocity = CheatClient.me().isOnGround() ? actualVelocity.multiply(2, 1, 2) : actualVelocity;

            return this.getSpeed(fasterVelocity) > this.maxSpeed ? actualVelocity : fasterVelocity;
        } else if (CheatClient.me().input.pressingForward || CheatClient.me().input.pressingBack) {
            if (CheatClient.me().input.pressingBack) rotVector = rotVector.multiply(-1, -1, -1);

            fasterVelocity = rotVector.multiply(maxSpeed, 0, maxSpeed);
            fasterVelocity = fasterVelocity.add(0, actualVelocity.y, 0);
        }

        return this.getSpeed(fasterVelocity) > this.maxSpeed ? actualVelocity : fasterVelocity;
    }

    @Override
    public void recieveEvent(String eventName, Object[] args) {
        if (!this.isEnabled()) return;

        switch(eventName) {
            case "onMovementPacket": {
                CallbackInfo ci = (CallbackInfo)args[0];

                if (CheatClient.me().isOnGround()) {
                    Vec3d newVelocity = this.getNewVelocity();
                    CheatClient.me().setVelocity(newVelocity);

                    ci.cancel();
                }
                break;
            }
            case "onTick": {
                if (!(CheatClient.me().input.pressingForward || CheatClient.me().input.pressingBack || CheatClient.me().input.pressingLeft || CheatClient.me().input.pressingRight)) {
                    // Still... but not flying...
                    Vec3d curr = CheatClient.me().getVelocity();
                    
                    CheatClient.me().setVelocity(curr.multiply(new Vec3d(0, 1, 0)));
                }
                break;
            }
        }
    }
}