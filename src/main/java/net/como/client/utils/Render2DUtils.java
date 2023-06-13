package net.como.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.ComoClient;
import net.como.client.misc.Colour;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import net.minecraft.util.math.Vec2f;

public class Render2DUtils {
    public static class BufferContainer {
        Matrix4f matrix;
        BufferBuilder bufferBuilder;
        Tessellator tessellator;

        private void open(MatrixStack matrixStack, VertexFormat.DrawMode drawMode, VertexFormat format) {
            this.matrix = matrixStack.peek().getPositionMatrix();
            this.tessellator = RenderSystem.renderThreadTesselator();
            this.bufferBuilder = tessellator.getBuffer();

            RenderSystem.setShader(GameRenderer::getPositionProgram);

            this.bufferBuilder.begin(drawMode, format);
        }

        public BufferContainer(MatrixStack matrixStack, VertexFormat.DrawMode drawMode, VertexFormat format) {
            this.open(matrixStack, drawMode, format);
        }

        public BufferContainer(MatrixStack matrixStack) {
            this.open(matrixStack, VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        }

        public void close() {
            tessellator.draw();
        }

        public void vertex2D(int x, int y) {
            this.bufferBuilder.vertex(matrix, x, y, 0).next();
        }

        public void vertex2D(double x, double y) {
            this.bufferBuilder.vertex(matrix, (int)x, (int)y, 0).next();
        }
    }

    public static Vec2f relPosition(Vec2f vec) {
        return relPosition((int)vec.x, (int)vec.y);
    }

    public static Vec2f relPosition(int x, int y) {
        return new Vec2f(
            (float)x,
            (float)y
        );
    }
    
    public static void begin(MatrixStack matrixStack, Colour colour) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		matrixStack.push();
        RenderSystem.setShaderColor(
            colour.r / 255f,
            colour.g / 255f,
            colour.b / 255f,
            colour.a / 255f
        );
    }

    public static void finish(MatrixStack matrixStack) {
		matrixStack.pop();

		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void renderLine(MatrixStack matrixStack, int x, int y, int x2, int y2, Colour colour) {
        begin(matrixStack, colour);
        BufferContainer bufferContainer = new BufferContainer(matrixStack);

        bufferContainer.vertex2D(x, y);
        bufferContainer.vertex2D(x2, y2);

        bufferContainer.close();
        finish(matrixStack);
    }

    public static void renderLine(MatrixStack matrixStack, int x, int y, int x2, int y2) {
        renderLine(matrixStack, x, y, x2, y2, new Colour(255, 255, 255, 255));
    }

    public static void renderBox(MatrixStack matrixStack, int x, int y, int x2, int y2, Colour colour) {
        begin(matrixStack, colour);
        BufferContainer bufferContainer = new BufferContainer(matrixStack);

        // Top
        bufferContainer.vertex2D(x, y);
        bufferContainer.vertex2D(x2, y);

        // Right
        bufferContainer.vertex2D(x2, y);
        bufferContainer.vertex2D(x2, y2);

        // Left
        bufferContainer.vertex2D(x, y);
        bufferContainer.vertex2D(x, y2);

        // Bottom
        bufferContainer.vertex2D(x, y2);
        bufferContainer.vertex2D(x2, y2);

        bufferContainer.close();
        finish(matrixStack);
    }

    public static void renderBox(MatrixStack matrixStack, int x, int y, int x2, int y2) {
        renderBox(matrixStack, x, y, x2, y2, new Colour(255, 255, 255, 255));
    }

    public static void renderBackground(MatrixStack matrixStack, int x, int y, int x2, int y2, Colour colour) {
        begin(matrixStack, colour);
        BufferContainer bufferContainer = new BufferContainer(matrixStack, VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        bufferContainer.vertex2D(x, y);
        bufferContainer.vertex2D(x, y2);
        bufferContainer.vertex2D(x2, y2);
        bufferContainer.vertex2D(x2, y);

        bufferContainer.close();
        finish(matrixStack);
    }

    public static void renderBackgroundBox(MatrixStack matrixStack, int x, int y, int x2, int y2, Colour bgColour, Colour outline) {
        renderBackground(matrixStack, x, y, x2, y2, bgColour);
        renderBox(matrixStack, x, y, x2, y2, outline);
    }

    public static void renderBoxShadow(MatrixStack matrixStack, int x, int y, int x2, int y2, Colour shadowColour) {
        Vec2f size = new Vec2f(x2 - x, y2 - y);
        
        int sideWidth = 2;
        int bottomWidth = 4;

        int offsetY = (int)(size.y) - bottomWidth;
        renderBackground(matrixStack, x, y + offsetY, x2 - sideWidth, y2, shadowColour);
        renderBackground(matrixStack, x2 - sideWidth, y, x2, y2, shadowColour);
    }

    public static void renderBoxShadow(MatrixStack matrixStack, int x, int y, int x2, int y2) {
        renderBoxShadow(
            matrixStack, x, y, x2, y2, new Colour(0, 0, 0, 5)
        );
    }

    public static void renderSimpleText(MatrixStack matrixStack, String text, int x, int y, float scale, Colour colour) {
        begin(matrixStack, new Colour(255, 255, 255, 255));
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, 1);
        
        ComoClient.getInstance().textRenderer.drawWithShadow(
            matrixStack,
            Text.of(text),
            0,
            0,
            colour.toARGB()
        );

        finish(matrixStack);
    }

    public static void renderSimpleText(MatrixStack matrixStack, String text, int x, int y, Colour colour) {
        renderSimpleText(matrixStack, text, x, y, 1, colour);
    }

    public static void renderHeart(MatrixStack matrixStack, int x, int y) {
        int u = 16 + (2 * 2 + 0) * 9;

        DrawableHelper.drawTexture(matrixStack, x, y, u, 9, 9, 9, 256, 256);
        DrawableHelper.drawTexture(matrixStack, x, y, u, 0, 9, 9, 256, 256);
    }

    public static void renderCircle(MatrixStack matrixStack, double x, double y, double radius, Colour colour) {
        // Render a circle using opengl

        begin(matrixStack, colour);
        BufferContainer bufferContainer = new BufferContainer(matrixStack);

        for (double i = 0; i < 360; i += 0.01d) {
            double angle = Math.toRadians(i);
            double x1 = x + Math.cos(angle) * radius;
            double y1 = y + Math.sin(angle) * radius;

            bufferContainer.vertex2D(x1, y1);
        }

        bufferContainer.close();
        finish(matrixStack);
    }
}
