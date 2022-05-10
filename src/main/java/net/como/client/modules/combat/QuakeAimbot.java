package net.como.client.modules.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.RenderWorldEvent;
import net.como.client.interfaces.mixin.IClient;
import net.como.client.modules.exploits.OffHandCrash;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.RotationUtils;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class QuakeAimbot extends Module {
    Random random = new Random();

    @Override
    public String listOption() {
        if (!this.getBoolSetting("AutoShoot")) return super.listOption();

        return this.shooting ? "Shooting" : "Idle";
    }

    public QuakeAimbot() {
        super("QuakeAimbot");

        // Targetting
        this.addSetting(new Setting("Range", 50d));
        this.addSetting(new Setting("FOV", 3d));
        this.addSetting(new Setting("Headshot", true));
        this.addSetting(new Setting("Predict", true));
        this.addSetting(new Setting("PredictStep", 1d));

        // Smoothing
        this.addSetting(new Setting("Smoothing", true));
        this.addSetting(new Setting("SmoothingStep", 5d));
        
        // Legit
        this.addSetting(new Setting("Randomise", true));
        this.addSetting(new Setting("RandomiseAmount", 0.125d));

        // Auto shoot
        this.addSetting(new Setting("AutoShoot", true));
        this.addSetting(new Setting("ShootDelay", 0.1d));
        this.addSetting(new Setting("ShootAngle", 1d));
    }
    
    @Override
    public void activate() {
        this.addListen(RenderWorldEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWorldEvent.class);
    }

    private Vec3d getTargetPos(Entity target, float tickDelta) {
        Vec3d offset = this.getBoolSetting("Headshot") 
            ? target.getEyePos()
            : target.getBoundingBox().getCenter();
        
        offset = offset.subtract(target.getPos());
        
        // Doesn't really work lol
        Vec3d predictOffset = this.getBoolSetting("Predict") ? target.getVelocity().multiply(this.getDoubleSetting("PredictStep")) : Vec3d.ZERO;
        
        return target.getLerpedPos(tickDelta).add(offset).add(predictOffset);
    }

    private Vec3d getTargetPos(Entity target) {
        return this.getTargetPos(target, 0);
    }

    private List<Entity> getTargets() {
        List<Entity> players = new ArrayList<>();

        Vec3d localPos = ComoClient.me().getPos();
        double fov = this.getDoubleSetting("FOV");

        // Get all of the players
        for (Entity ent : ComoClient.getClient().world.getEntities()) {
            if (!(ent instanceof PlayerEntity)) continue;

            Entity player = ent;

            // Make sure the player is not us
            if (player == ComoClient.me()) continue;

            // Make sure the player is not dead
            if (!player.isAlive()) continue;

            // Make sure the player is not invisible
            if (player.isInvisible()) continue;

            // Get the player's position
            Vec3d pos = this.getTargetPos(player);

            // Check that the player is in range
            if (localPos.distanceTo(pos) > this.getDoubleSetting("Range")) continue;

            // Make sure the player is visible
            if (!ComoClient.me().canSee(player)) continue;

            // Make sure the player is in the FOV
            Rotation current = new Rotation(ComoClient.me().getYaw(), ComoClient.me().getPitch());
            Rotation rotation = RotationUtils.getRequiredRotation(pos).difference(current);
            if (rotation.magnitude() > fov) continue;

            // Add the player to the list
            players.add(player);
        }

        return players;
    }

    double lastShootTime = 0;

    private Entity getTarget() {
        List<Entity> players = this.getTargets();

        // Make sure there are players
        if (players.isEmpty()) return null;

        // Sort the list by the closest to the player's angle
        players.sort((a, b) -> {
            Vec3d aPos = a.getPos();
            Vec3d bPos = b.getPos();

            Rotation aRotation = RotationUtils.getRequiredRotation(aPos);
            Rotation bRotation = RotationUtils.getRequiredRotation(bPos);

            return (int)(aRotation.magnitude() - bRotation.magnitude());
        });

        // Return the closest player
        return players.get(0);
    }

    private boolean shooting = false;
    
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldEvent": {
                Entity target = this.getTarget();

                // Make sure we have a target
                if (target == null) {
                    this.shooting = false;
                    return;
                }

                float tickDelta = ((RenderWorldEvent)event).tickDelta;

                // Calculate random offset
                Vec3d offset = Vec3d.ZERO;
                if (this.getBoolSetting("Randomise")) {
                    double max = this.getDoubleSetting("RandomiseAmount");

                    double x = this.random.nextDouble() * max;
                    double y = this.random.nextDouble() * max;
                    double z = this.random.nextDouble() * max;

                    offset = offset.add(x, y, z);
                }

                // Get the target's position
                Vec3d targetPos = this.getTargetPos(target, tickDelta);

                // Apply random offset
                targetPos = targetPos.add(offset);

                // Get the target's rotation
                Rotation targetRotation = RotationUtils.getRequiredRotation(targetPos);
                Rotation current = new Rotation(ComoClient.me().getYaw(), ComoClient.me().getPitch());
                Rotation diff = targetRotation.difference(current);

                // Get the pitch and yaw
                float pitch = (float)targetRotation.pitch;
                float yaw   = (float)targetRotation.yaw;

                // Apply the step
                if (this.getBoolSetting("Smoothing")) {
                    double step = this.getDoubleSetting("SmoothingStep");

                    pitch = (float)(current.pitch + diff.pitch / step);
                    yaw   = (float)(current.yaw   + diff.yaw   / step);
                }

                // Set the pitch and yaw
                ComoClient.me().setPitch(pitch);
                ComoClient.me().setYaw(yaw);

                // Shoot
                this.shooting = false;
                if (this.getBoolSetting("AutoShoot")) {
                    if (diff.magnitude() > this.getDoubleSetting("ShootAngle")) break;

                    this.shooting = true;                    

                    // Shooting delay
                    double curr = ComoClient.getCurrentTime();

                    if (this.lastShootTime + this.getDoubleSetting("ShootDelay") > curr) break;
                    this.lastShootTime = curr;

                    // Shoot (i.e. right click)
                    MinecraftClient client = ComoClient.getClient();
                    IClient clientAccessor = (IClient)client;
                    clientAccessor.performItemUse();
                }

                break;
            }
        }
    }
}
