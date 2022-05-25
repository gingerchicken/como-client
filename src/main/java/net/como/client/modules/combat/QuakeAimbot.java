package net.como.client.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.render.RenderWorldEvent;
import net.como.client.interfaces.mixin.IClient;
import net.como.client.misc.Colour;
import net.como.client.misc.Module;
import net.como.client.misc.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.RenderUtils;
import net.como.client.utils.RotationUtils;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
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

        this.setDescription("A Basic Hypixel Quakecraft Aimbot");

        this.setCategory("Combat");

        // Targetting
        this.addSetting(new Setting("Range", 128d));
        this.addSetting(new Setting("FOV", 180d));
        this.addSetting(new Setting("Headshot", true));
        this.addSetting(new Setting("IgnoreTeamMates", true));

        // Prediction
        this.addSetting(new Setting("Predict", true));
        this.addSetting(new Setting("PredictStep", 4d));
        this.addSetting(new Setting("Preaim", true));
        this.addSetting(new Setting("PredictBlockWall", true));

        this.addSetting(new Setting("LocalBacktrack", false));
        this.addSetting(new Setting("BacktrackStep", 5));
        this.addSetting(new Setting("BacktrackRenderSteps", false));

        // Smoothing
        this.addSetting(new Setting("Smoothing", true));
        this.addSetting(new Setting("SmoothingStep", 50d));
        this.addSetting(new Setting("SmoothingIgnoreFOV", 1d));
        
        // Legit
        this.addSetting(new Setting("Randomise", false));
        this.addSetting(new Setting("RandomiseAmount", 0.125d));

        // Auto shoot
        this.addSetting(new Setting("AutoShoot", true));
        this.addSetting(new Setting("ShootDelay", 0d));
        this.addSetting(new Setting("ShootAngle", 1d));
    }
    
    @Override
    public void activate() {
        this.addListen(RenderWorldEvent.class);
        this.addListen(ClientTickEvent.class);
        this.addListen(RenderWorldEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWorldEvent.class);
        this.removeListen(ClientTickEvent.class);
        this.removeListen(RenderWorldEvent.class);
    }

    private Vec3d getTargetOffset(Entity target) {
        Vec3d offset = this.getBoolSetting("Headshot") 
            ? target.getEyePos()
            : target.getBoundingBox().getCenter();
        
        return offset.subtract(target.getPos());
    }

    /**
     * Gets a lerped position of the target
     * @param target the target
     * @param tickDelta the tick delta
     * @return lerped position
     */
    private Vec3d getTargetPos(Entity target, float tickDelta) {
        if (this.getBoolSetting("Predict")) return this.getPredictPos(target, tickDelta);

        // Get the target's position
        Vec3d offset = this.getTargetOffset(target);

        // Lerp it
        return target.getLerpedPos(tickDelta).add(offset);
    }

    private Vec3d getPredictPos(Entity target, float tickDelta) {
        Vec3d offset = this.getTargetOffset(target);

        // Get the target's position
        Vec3d targetPos = target.getLerpedPos(tickDelta).add(offset);

        // Calculate how much extrapolation we need
        float extrapolationAmount = 1 * (float)(double)(Double)this.getDoubleSetting("PredictStep");

        // Add the offset and extrapolate
        Vec3d predictedPos = target.getLerpedPos(extrapolationAmount).add(offset);

        // Check that the player wants to account for walls
        if (!this.getBoolSetting("PredictBlockWall")) return predictedPos;

        // Check if there is anything in the way of this position
        BlockHitResult hitResult = target.world.raycast(
            new RaycastContext(
                targetPos,
                predictedPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                target
            )
        );

        // If they can see it then return the predicted position
        if (hitResult.getType() == Type.MISS) return predictedPos;

        // If they can't then we need to find the closet location to that where they can
        Vec3d pos = hitResult.getPos();

        // Return this position
        return pos;
    }

    /**
     * Gets a non-lerped position of the target
     * @param target The target
     * @return position
     */
    private Vec3d getTargetPos(Entity target) {
        return this.getTargetPos(target, 0);
    }

    /**
     * Gets a player's armour colour
     * @param player the player
     * @return colour, -1 if no armour was found
     */
    private int getTeamColour(Entity player) {
        // Get player armour
        for (ItemStack itemStack : player.getArmorItems()) {
            Item item = itemStack.getItem();

            // Make sure it is a dyeable armour
            if (!(item instanceof DyeableArmorItem)) continue;

            // Get the colour
            return ((DyeableArmorItem)item).getColor(itemStack);
        }

        // Return if no colour was found
        return -1;
    }

    /**
     * Gets an array of targets
     * @return array of targets
    */
    private List<Entity> getTargets() {
        List<Entity> players = new ArrayList<>();

        Vec3d localPos = ComoClient.me().getPos();
        double fov = this.getDoubleSetting("FOV");
        int localTeamColour = this.getTeamColour(ComoClient.me());

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
            boolean canSee = this.getBoolSetting("Preaim") ? ClientUtils.canSee(pos) : ComoClient.me().canSee(player);
            if (!canSee) continue;

            // Make sure the player is in the FOV
            Rotation current = new Rotation(ComoClient.me().getYaw(), ComoClient.me().getPitch());
            Rotation rotation = RotationUtils.getRequiredRotation(pos).difference(current);
            if (rotation.magnitude() > fov) continue;

            // Checks if they are in the same team
            if (localTeamColour != -1 && this.getBoolSetting("IgnoreTeamMates") && this.getTeamColour(player) == localTeamColour) continue;

            // Add the player to the list
            players.add(player);
        }

        return players;
    }

    private double lastShootTime = 0;

    /**
     * Gets the target
     * @return target
     */
    private Entity getTarget() {
        List<Entity> players = this.getTargets();

        // Get current rotation
        Rotation current = new Rotation(ComoClient.me().getYaw(), ComoClient.me().getPitch());

        // Make sure there are players
        if (players.isEmpty()) return null;

        // Sort the list by the closest to the player's angle
        players.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity a, Entity b) {
                Vec3d aPos = getTargetPos(a);
                Vec3d bPos = getTargetPos(b);
    
                Rotation aRotation = RotationUtils.getRequiredRotation(aPos);
                Rotation bRotation = RotationUtils.getRequiredRotation(bPos);
    
                // Calculate the distances
                double aDist = aRotation.difference(current).magnitude();
                double bDist = bRotation.difference(current).magnitude();

                return (int)(aDist - bDist);
            }
        });

        // Return the closest player
        return players.get(0);
    }

    /**
     * Checks if the aimbot's triggerbot is firing
     */
    private boolean shooting = false;

    /**
     * Backtrack position queue
     */
    private Queue<Vec3d> previousPositions = new LinkedList<>();

    /**
     * Adds a new position to the backtrack queue
     * @param pos the position
     */
    private void addNewBacktrackPos(Vec3d pos) {
        int backtrackLength = this.getIntSetting("BacktrackStep");
        
        while (this.previousPositions.size() > backtrackLength) {
            this.previousPositions.remove();
        }

        previousPositions.add(pos);
    }

    /**
     * Get the last backtrack position
     * @return the last backtrack position
     */
    private Vec3d getBacktrackPos() {
        if (this.previousPositions.isEmpty()) return this.getLocalPos();

        return this.previousPositions.peek();
    }

    /**
     * Add the current position to the backtrack list
     */
    private void addCurrentBacktrackPos() {
        this.addNewBacktrackPos(RotationUtils.getEyePos());
    }

    /**
     * Gets the local position alongside the backtracked position
     * @param tickDelta the tick delta
     * @return the local position
     */
    private Vec3d getLocalPos(float tickDelta) {
        // Get the player's position
        Vec3d pos = this.getLocalPos();

        // Check for backtrack
        if (this.getBoolSetting("LocalBacktrack")) {
            // Get the backtracked position
            pos = this.getBacktrackPos();
        }

        return pos;
    }

    /**
     * Gets a standard position without backtrack
     * @return the standard position
     */
    public Vec3d getLocalPos() {
        return RotationUtils.getEyePos();
    }

    /**
     * Aimbot logic
     * @param tickDelta the tick delta
     */
    private void aimbotThink(float tickDelta) {
        Entity target = this.getTarget();

        // Make sure we have a target
        if (target == null) {
            this.shooting = false;
            return;
        }

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

        // Get the local position
        Vec3d localPos = this.getLocalPos(tickDelta);

        // Get the target's rotation
        Rotation targetRotation = RotationUtils.getRequiredRotation(localPos, targetPos, tickDelta);
        Rotation current = new Rotation(ComoClient.me().getYaw(), ComoClient.me().getPitch());
        Rotation diff = targetRotation.difference(current);

        // Get the pitch and yaw
        float pitch = (float)targetRotation.pitch;
        float yaw   = (float)targetRotation.yaw;

        // Apply the step
        if (this.getBoolSetting("Smoothing")) {
            double step = this.getDoubleSetting("SmoothingStep");
            double antiSmoothingFOV = this.getDoubleSetting("SmoothingIgnoreFOV");

            if (diff.magnitude() > antiSmoothingFOV) {
                pitch = (float)(current.pitch + diff.pitch / step);
                yaw   = (float)(current.yaw   + diff.yaw   / step);
            }
        }

        // Set the pitch and yaw
        ComoClient.me().setPitch(pitch);
        ComoClient.me().setYaw(yaw);

        // Shoot
        this.shooting = false;
        if (this.getBoolSetting("AutoShoot")) {
            if (diff.magnitude() > this.getDoubleSetting("ShootAngle")) return;

            this.shooting = true;                    

            // Shooting delay
            double curr = ComoClient.getCurrentTime();

            if (this.lastShootTime + this.getDoubleSetting("ShootDelay") > curr) return;
            this.lastShootTime = curr;

            // Shoot (i.e. right click)
            MinecraftClient client = ComoClient.getClient();
            IClient clientAccessor = (IClient)client;
            clientAccessor.performItemUse();
        }
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                // Update the backtrack position
                this.addCurrentBacktrackPos();

                break;
            }
            case "RenderWorldEvent": {
                // Get tick delta
                float tickDelta = ((RenderWorldEvent)event).tickDelta;

                // Get mStack
                MatrixStack mStack = ((RenderWorldEvent)event).mStack;

                // Render backtrack
                if (this.getBoolSetting("LocalBacktrack") && this.getBoolSetting("BacktrackRenderSteps")) {
                    Colour chosen = new Colour(255, 0, 0, 255);
                    Colour normal = new Colour(255, 255, 255, 255);
                    for (Vec3d pos : this.previousPositions) {
                        boolean isChosenPosition = pos.equals(this.getBacktrackPos());

                        // Get the colour
                        Colour c = isChosenPosition ? chosen : normal;

                        // Render the position
                        RenderUtils.renderBlockBox(mStack, pos, c);
                    }
                }

                // Do the aimbot (this SHOULD not be done inside of a render thread)
                this.aimbotThink(tickDelta);

                break;
            }
        }
    }
}
