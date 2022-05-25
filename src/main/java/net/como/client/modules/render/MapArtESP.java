package net.como.client.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;

import net.como.client.ComoClient;
import net.como.client.events.render.RenderWorldEvent;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;
import net.como.client.misc.settings.Setting;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;

public class MapArtESP extends Module {
    public MapArtESP() {
        super("MapArtESP");
        
        this.setDescription("Outlines the edges of a map.");

        this.addSetting(new Setting("ShowSides", true) {{
            this.setDescription("Shows the sides of the map (e.g. bottom, top, left, right.)");
        }});

        this.addSetting(new Setting("FollowHeight", true) {{
            this.setDescription("Side labels will follow the player's height.");
        }});

        this.addSetting(new Setting("LabelHeight", 32d) {{
            this.setDescription("The offset height of the side labels.");
        }});

        this.addSetting(new Setting("LabelScale", 15d) {{
            this.setDescription("The scale of the side labels.");

            this.setMin(0d);
            this.setMax(64d);
        }});

        this.setCategory("Render");
    }
    
    @Override
    public void activate() {
        this.addListen(RenderWorldEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWorldEvent.class);
    }

    /**
     * Calculates where the map is going to be rendered
     * @return The position of the map area
     */
    private BlockPos getMapRegion() {
        int size = 128;

        BlockPos me = ComoClient.me().getBlockPos();

        // Get player center
        int centerX = me.getX();
        int centerZ = me.getZ();

        centerX = MathHelper.floor((centerX + 64.0) / (double)size);
        centerZ = MathHelper.floor((centerZ + 64.0) / (double)size);

        // Find x and z
        int x = centerX * size + size / 2 - 64;
        int z = centerZ * size + size / 2 - 64;

        return new BlockPos(x, 0, z);
    }

    private void translateMapRegion(MatrixStack matrixStack) {
        matrixStack.translate(getMapRegion().getX() - 64, 0, getMapRegion().getZ() - 64);
    }


    /**
     * Render a given side
     * @param matrixStack The matrix stack
     * @param textRenderer The text renderer
     * @param scale The scale
     */
    private void renderSide(MatrixStack matrixStack, TextRenderer textRenderer, Quaternion quaternion, String sideName, float scale) {
        matrixStack.push();

        matrixStack.multiply(quaternion);
        matrixStack.scale(scale*-0.025F, scale*-0.025F, 0);

        textRenderer.draw(matrixStack, sideName, -textRenderer.getWidth(sideName) / 2, 0, 0xFFFFFF);

        matrixStack.pop();
    }


    /**
     * Render the bottom side
     * @param matrixStack The matrix stack
     * @param textRenderer The text renderer
     * @param scale The scale
     */
    private void renderBottom(MatrixStack matrixStack, TextRenderer textRenderer, float scale) {
        matrixStack.push();

        matrixStack.translate(64, 0, 128);
        renderSide(matrixStack, textRenderer, new Quaternion(0, 0, 0, 0), "Bottom", scale);

        matrixStack.pop();
    }

    /**
     * Render the top side
     * @param matrixStack The matrix stack
     * @param textRenderer The text renderer
     * @param scale The scale
     */
    private void renderTop(MatrixStack matrixStack, TextRenderer textRenderer, float scale) {
        matrixStack.push();

        matrixStack.translate(64, 0, 0);
        renderSide(matrixStack, textRenderer, new Quaternion(0, 1, 0, 0), "Top", scale);

        matrixStack.pop();
    }


    /**
     * Render the left side
     * @param matrixStack The matrix stack
     * @param textRenderer The text renderer
     * @param scale The scale
     */
    private void renderLeft(MatrixStack matrixStack, TextRenderer textRenderer, float scale) {
        matrixStack.push();

        matrixStack.translate(0, 0, 64);
        renderSide(matrixStack, textRenderer, new Quaternion(0, 0.7f, 0, -0.7f), "Left", scale);

        matrixStack.pop();
    }

    /**
     * Render the right side
     * @param matrixStack The matrix stack
     * @param textRenderer The text renderer
     * @param scale The scale
     */
    private void renderRight(MatrixStack matrixStack, TextRenderer textRenderer, float scale) {
        matrixStack.push();

        matrixStack.translate(128, 0, 64);
        renderSide(matrixStack, textRenderer, new Quaternion(0, 0.7f, 0, 0.7f), "Right", scale);

        matrixStack.pop();
    }

    private void renderSides(MatrixStack matrixStack) {
        float scale = (float)(double)this.getDoubleSetting("LabelScale");

        TextRenderer textRenderer = ComoClient.getClient().textRenderer;
        
        matrixStack.push();

        RenderUtils.applyRenderOffset(matrixStack);
        translateMapRegion(matrixStack);

        this.renderTop(matrixStack, textRenderer, scale);
        this.renderBottom(matrixStack, textRenderer, scale);
        this.renderLeft(matrixStack, textRenderer, scale);
        this.renderRight(matrixStack, textRenderer, scale);

        matrixStack.pop();
    }

    private void renderBoundaries(MatrixStack matrixStack) {
        // Create the box
        double y = ComoClient.me().getBlockPos().getY();
        double outY = 1280;

        BlockPos min = new BlockPos(0, y - outY, 0);
        BlockPos max = new BlockPos(min.getX() + 128, y + outY, min.getZ() + 128);

        Box borderBox = new Box(min, max);

        // Render the box
        matrixStack.push();

        // Translate position
        RenderUtils.applyRenderOffset(matrixStack);
        
        // Translate map region
        this.translateMapRegion(matrixStack);

        // Render the box
        RenderUtils.drawOutlinedBox(borderBox, matrixStack);

        // Stop rendering
        matrixStack.pop();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldEvent": {
                RenderWorldEvent e = (RenderWorldEvent) event;

                // Get the matrix stack
                MatrixStack matrixStack = e.mStack;
                
                this.renderBoundaries(matrixStack);

                if (this.getBoolSetting("ShowSides")) {
                    matrixStack.push();

                    // Translate to correct height
                    double labelHeight = this.getDoubleSetting("LabelHeight");
                    matrixStack.translate(0, labelHeight, 0);

                    // Follow the player
                    if (this.getBoolSetting("FollowHeight")) {
                        matrixStack.translate(0, e.camera.getPos().y, 0);
                    }

                    // Render the sides
                    this.renderSides(matrixStack);

                    matrixStack.pop();
                }

                break;
            }
        }
    }
}
