package net.como.client.gui.impl.widgets;

import net.como.client.gui.Widget;
import net.como.client.modules.hud.Watermark;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class BouncyWidget implements Widget {

    /**
     * The position of the bouncy<br>
     * <p>x = x position</p>
     * <p>y = y position</p>
     * <p>z = scale</p>
     */
    private Vec3d pos = Vec3d.ZERO;

    private final int screenWidth, screenHeight;
    private int width, height;

    private Vec3d velocity;

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Vec3d currentPos = this.pos;
        Vec3d nextPos = this.getNextPosition();

        Vec3d lerpedPos = currentPos.lerp(nextPos, delta);

        Watermark.render(matrices, lerpedPos.z, lerpedPos.x, lerpedPos.y);
    }

    /**
     * Calculates the next velocity according to the current velocity and the current position, using hit detection
     * @return The next velocity
     */
    private Vec3d getNextVelocity() {
        Vec3d velocity = this.velocity;

        // Bouncy!
        if (this.pos.x <= 0 || this.pos.x >= this.screenWidth || this.pos.x + this.width >= this.screenWidth || this.pos.x + this.width <= 0) {
            velocity = new Vec3d(-velocity.x, velocity.y, velocity.z);
        }

        if (this.pos.y <= 0 || this.pos.y >= this.screenHeight || this.pos.y + this.height >= this.screenHeight || this.pos.y + this.height <= 0) {
            velocity = new Vec3d(velocity.x, -velocity.y, velocity.z);
        }

        // Scaling
        if (this.pos.z <= 0 || this.pos.z >= 1) {
            velocity = new Vec3d(velocity.x, velocity.y, -velocity.z);
        }

        return velocity;
    }

    /**
     * Gets the next position of the bouncy according to the next velocity
     * @return The next position of the bouncy
     */
    private Vec3d getNextPosition() {
        return this.getNextPosition(this.getNextVelocity());
    }

    /**
     * Gets the next position of the bouncy with a given velocity
     * @param velocity The velocity of the bouncy
     * @return The next position of the bouncy
     */
    private Vec3d getNextPosition(Vec3d velocity) {
        return this.pos.add(velocity);
    }

    @Override
    public void tick() {
        // Update the velocity
        this.velocity = this.getNextVelocity();

        // Update the position
        this.pos = this.getNextPosition(this.velocity);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    public BouncyWidget(int parentWidth, int parentHeight, Vec3d pos, Vec3d velocity) {
        this.screenWidth = parentWidth;
        this.screenHeight = parentHeight;
        this.pos = pos;
        this.velocity = velocity;
    }

    /**
     * Intialises the bouncy with a random position and a random velocity
     * @param parentWidth The width of the parent
     * @param parentHeight The height of the parent
     * @param scale The scale of the bouncy
     */
    public BouncyWidget(int parentWidth, int parentHeight, double scale) {
        this.screenWidth  = parentWidth;
        this.screenHeight = parentHeight;

        // Setup watermark size
        this.width  = Watermark.BACKGROUND_WIDTH  / 6;
        this.height = Watermark.BACKGROUND_HEIGHT / 6;

        // Setup watermark position as a random position, making sure it is not stuck in the sides
        this.pos = new Vec3d(
                (int)(Math.random() * (parentWidth - this.width)),
                (int)(Math.random() * (parentHeight - this.height)),
                scale
        );
        
        // Set the velocity as a random velocity
        //  - with a random direction
        //  - round each component to the nearest integer (-1 and 1)
        this.velocity = new Vec3d(
                (int)(Math.random() * 3) - 1,
                (int)(Math.random() * 3) - 1,
                0
        ).multiply(1);

    }

    /**
     * Get the scale of the bouncy
     * @return scale
     */
    public double getScale() {
        return this.pos.z;
    }
}
