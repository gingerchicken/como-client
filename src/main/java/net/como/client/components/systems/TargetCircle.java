package net.como.client.components.systems;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.components.ProjectionUtils;
import net.como.client.misc.Colour;
import net.como.client.misc.maths.Vec3;
import net.como.client.utils.Render2DUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class TargetCircle {
    private Colour colour;
    private double fov;

    /**
     * Gets the colour of the circle.
     * @return The colour of the circle.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Creates a new circle with a given colour
     * @param colour
     */
    public TargetCircle(Colour colour) {
        this.colour = colour;
    }

    /**
     * Creates a new white circle
     */
    public TargetCircle() {
        this(new Colour(255, 255, 255, 255));
    }

    /**
     * Checks if the entity is in the field of view.
     * @param entityPos The position of the entity.
     * @return True if the entity is in the field of view, false otherwise.
     */
    public boolean isInCircle(Vec3d entityPos) {
        if (fov >= 360) {
            return true;
        }

        Vec3 pos = new Vec3(entityPos);

        ProjectionUtils.unscaledProjection();

        // Get the 2D position
        boolean visible = ProjectionUtils.getInstance().to2D(pos, 1);
        ProjectionUtils.resetProjection();

        if (!visible) return false;

        MinecraftClient client = ComoClient.getClient();

        // Find distance from the center of the screen
        Vec3 center = new Vec3(
            client.getWindow().getWidth()  / 2,
            client.getWindow().getHeight() / 2,
            0
        );

        pos.z = 0;

        // Get the distance
        double distance = pos.distanceTo(center);

        // Check if it is in the circle
        return distance / 2 <= this.getRadius();
    }

    /**
     * Set the FOV of the circle.
     * @param fov The FOV of the circle.
     */
    public void setFov(double fov) {
        this.fov = fov;
    }

    /**
     * Gets the FOV of the circle.
     * @return The FOV of the circle.
     */
    public double getFov() {
        return fov;
    }

    /**
     * Gets the radius of the circle.
     * @return The radius of the circle.
     */
    public double getRadius() {
        return fov * ComoClient.getClient().getWindow().getScaledWidth() / ComoClient.getClient().options.fov;
    }

    /**
     * Gets all entities in the circle.
     * @return A list of entities in the circle.
     */
    public Iterable<Entity> getEntities() {
        List<Entity> ents = new ArrayList<>();

        for (Entity entity : ComoClient.getClient().world.getEntities()) {
            if (entity == ComoClient.me()) continue;

            if (!isInCircle(entity.getPos())) continue;

            ents.add(entity);
        }

        return ents;
    }

    /**
     * Render the circle.
     * @param matrixStack The matrix stack.
     */
    public void render(MatrixStack matrixStack) {
        // Get the window

        Window window = MinecraftClient.getInstance().getWindow();
        
        double width  = window.getScaledWidth();
        double height = window.getScaledHeight();

        Render2DUtils.renderCircle(matrixStack, width / 2, height / 2, this.getRadius(), colour);
    }
}
