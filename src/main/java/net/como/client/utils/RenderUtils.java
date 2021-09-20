/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.como.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.CheatClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.chunk.Chunk;

public class RenderUtils {
	// TODO name this accordingly as currently I have no clue to what this actually does...
	public static Vec3d whackifyPos(Entity e, double regionX, double regionZ, double partialTicks) {
		return new Vec3d(
			e.prevX + (e.getX() - e.prevX) * partialTicks - regionX,
            e.prevY + (e.getY() - e.prevY) * partialTicks,
            e.prevZ + (e.getZ() - e.prevZ) * partialTicks - regionZ
		);
	}

	// TODO Cache this or remove it, this is for my dumb brain and debugging reasons
	public static void g11COLORRGB(Float r, Float g, Float b, Float a) {
		GL11.glColor4f(r/255f, g/255f, b/255f, a/255f);
	}

	private static final Box DEFAULT_BOX = new Box(0, 0, 0, 1, 1, 1);
	
	public static BlockPos getRegion() {
		BlockPos camPos = RenderUtils.getCameraBlockPos();
        int regionX = (camPos.getX() >> 9) * 512;
        int regionZ = (camPos.getZ() >> 9) * 512;

		return new BlockPos(regionX, 0, regionZ);
	}

	public static VertexBuffer simpleMobBox;
	static {
		simpleMobBox = new VertexBuffer();
		Box bb = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBox(bb, simpleMobBox);
	}

	private static void renderBox(Entity e, double partialTicks, MatrixStack mStack) {
		renderBox(e, partialTicks, mStack, true, 0f);
	}

	public static void renderBox(Entity e, double partialTicks, MatrixStack mStack, boolean blend, float extraSize) {
		// GL Settings
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if (blend) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

		// Get region
		BlockPos region = getRegion();

        // Render Section
        mStack.push();
        RenderUtils.applyRegionalRenderOffset(mStack);

        // Load the renderer
        RenderSystem.setShader(GameRenderer::getPositionShader);

        // Translate the point of rendering
        mStack.translate(
            e.prevX + (e.getX() - e.prevX) * partialTicks - region.getX(),
            e.prevY + (e.getY() - e.prevY) * partialTicks,
            e.prevZ + (e.getZ() - e.prevZ) * partialTicks - region.getZ()
        );
        
        // Update the size of the box.
        mStack.scale(e.getWidth() + extraSize, e.getHeight() + extraSize, e.getWidth() + extraSize);

        // Make the boxes change colour depending on their distance.
        float f = CheatClient.me().distanceTo(e) / 20F;
        RenderSystem.setShaderColor(2 - f, f, 0, 0.5F);
        
        // Make it so it is our mobBox.
        Shader shader = RenderSystem.getShader();
        Matrix4f matrix4f = RenderSystem.getProjectionMatrix();
        simpleMobBox.setShader(mStack.peek().getModel(), matrix4f, shader);
        
        // Pop the stack (i.e. render it)
        mStack.pop();

        // GL resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        if (blend) {
            GL11.glDisable(GL11.GL_BLEND);
        }
	}

	public static void drawLine3D(MatrixStack matrixStack, Vec3d start, Vec3d end) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		matrixStack.push();
		RenderUtils.applyRenderOffset(matrixStack);

		RenderSystem.setShaderColor(1, 1, 1, 0.5F);
		
		Matrix4f matrix = matrixStack.peek().getModel();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionShader);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
		bufferBuilder
			.vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
			.next();

		bufferBuilder
			.vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
			.next();
		
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);

		matrixStack.pop();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawTracer(MatrixStack matrixStack, Vec3d end) {
		RenderUtils.drawLine3D(
			matrixStack,
			RotationUtils.getClientLookVec().add(getCameraPos()),
			end
		);
	}

	public static void scissorBox(int startX, int startY, int endX, int endY)
	{
		int width = endX - startX;
		int height = endY - startY;
		int bottomY = CheatClient.getClient().currentScreen.height - endY;
		double factor = CheatClient.getClient().getWindow().getScaleFactor();
		
		int scissorX = (int)(startX * factor);
		int scissorY = (int)(bottomY * factor);
		int scissorWidth = (int)(width * factor);
		int scissorHeight = (int)(height * factor);
		GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
	}
	
	public static void applyRenderOffset(MatrixStack matrixStack)
	{
		applyCameraRotationOnly();
		Vec3d camPos = getCameraPos();
		
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
	}
	
	public static void applyRegionalRenderOffset(MatrixStack matrixStack)
	{
		applyCameraRotationOnly();
		
		Vec3d camPos = getCameraPos();
		BlockPos blockPos = getCameraBlockPos();
		
		int regionX = (blockPos.getX() >> 9) * 512;
		int regionZ = (blockPos.getZ() >> 9) * 512;
		
		matrixStack.translate(regionX - camPos.x, -camPos.y,
			regionZ - camPos.z);
	}
	
	public static void applyRegionalRenderOffset(MatrixStack matrixStack,
		Chunk chunk)
	{
		applyCameraRotationOnly();
		
		Vec3d camPos = getCameraPos();
		
		int regionX = (chunk.getPos().getStartX() >> 9) * 512;
		int regionZ = (chunk.getPos().getStartZ() >> 9) * 512;
		
		matrixStack.translate(regionX - camPos.x, -camPos.y,
			regionZ - camPos.z);
	}
	
	public static void applyCameraRotationOnly()
	{
		// no longer necessary for some reason
		
		// Camera camera =
		// CheatClient.getClient().getBlockEntityRenderDispatcher().camera;
		// GL11.glRotated(MathHelper.wrapDegrees(camera.getPitch()), 1, 0, 0);
		// GL11.glRotated(MathHelper.wrapDegrees(camera.getYaw() + 180.0), 0, 1,
		// 0);
	}
	
	public static Vec3d getCameraPos(float delta) {
		Entity ent = CheatClient.getClient().cameraEntity;

		Vec3d deltaPos = ent.getEyePos().add(
			ent.getPos().multiply(-1)
		);

		return ent.getLerpedPos(delta).add(deltaPos);
	}

	public static Vec3d getCameraPos() {
		return CheatClient.getClient().getBlockEntityRenderDispatcher().camera.getPos();
	}
	
	public static BlockPos getCameraBlockPos() {
		return CheatClient.getClient().getBlockEntityRenderDispatcher().camera
			.getBlockPos();
	}
	
	public static void drawSolidBox(MatrixStack matrixStack)
	{
		drawSolidBox(DEFAULT_BOX, matrixStack);
	}
	
	public static void drawSolidBox(Box bb, MatrixStack matrixStack)
	{
		Matrix4f matrix = matrixStack.peek().getModel();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
			VertexFormats.POSITION);
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	
	public static void drawSolidBox(Box bb, VertexBuffer vertexBuffer)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
			VertexFormats.POSITION);
		drawSolidBox(bb, bufferBuilder);
		bufferBuilder.end();
		
		vertexBuffer.upload(bufferBuilder);
	}
	
	public static void drawSolidBox(Box bb, BufferBuilder bufferBuilder)
	{
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
	}
	
	public static void drawOutlinedBox(MatrixStack matrixStack)
	{
		drawOutlinedBox(DEFAULT_BOX, matrixStack);
	}
	
	public static void drawOutlinedBox(Box bb, MatrixStack matrixStack)
	{
		Matrix4f matrix = matrixStack.peek().getModel();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	
	public static void drawOutlinedBox(Box bb, VertexBuffer vertexBuffer)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		drawOutlinedBox(bb, bufferBuilder);
		bufferBuilder.end();
		
		vertexBuffer.upload(bufferBuilder);
	}
	
	public static void drawOutlinedBox(Box bb, BufferBuilder bufferBuilder)
	{
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
	}
	
	public static void drawCrossBox(Box bb, MatrixStack matrixStack)
	{
		Matrix4f matrix = matrixStack.peek().getModel();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
			.next();
		
		bufferBuilder
			.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
			.next();
		bufferBuilder
			.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
			.next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	
	public static void drawCrossBox(Box bb, VertexBuffer vertexBuffer)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		drawCrossBox(bb, bufferBuilder);
		bufferBuilder.end();
		
		vertexBuffer.upload(bufferBuilder);
	}
	
	public static void drawCrossBox(Box bb, BufferBuilder bufferBuilder)
	{
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).next();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).next();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).next();
	}
	
	public static void drawNode(Box bb, MatrixStack matrixStack)
	{
		Matrix4f matrix = matrixStack.peek().getModel();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		double midX = (bb.minX + bb.maxX) / 2;
		double midY = (bb.minY + bb.maxY) / 2;
		double midZ = (bb.minZ + bb.maxZ) / 2;
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.maxZ)
			.next();
		bufferBuilder.vertex(matrix, (float)bb.minX, (float)midY, (float)midZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)bb.minX, (float)midY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.minZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.minZ)
			.next();
		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)midY, (float)midZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)midY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.maxZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.maxY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)midY, (float)midZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.maxY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)bb.minX, (float)midY, (float)midZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.maxY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.minZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.maxY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.maxZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.minY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)midY, (float)midZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.minY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)bb.minX, (float)midY, (float)midZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.minY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.minZ)
			.next();
		
		bufferBuilder.vertex(matrix, (float)midX, (float)bb.minY, (float)midZ)
			.next();
		bufferBuilder.vertex(matrix, (float)midX, (float)midY, (float)bb.maxZ)
			.next();
		
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	
	public static void drawNode(Box bb, VertexBuffer vertexBuffer)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		drawNode(bb, bufferBuilder);
		bufferBuilder.end();
		
		vertexBuffer.upload(bufferBuilder);
	}
	
	public static void drawNode(Box bb, BufferBuilder bufferBuilder)
	{
		double midX = (bb.minX + bb.maxX) / 2;
		double midY = (bb.minY + bb.maxY) / 2;
		double midZ = (bb.minZ + bb.maxZ) / 2;
		
		bufferBuilder.vertex(midX, midY, bb.maxZ).next();
		bufferBuilder.vertex(bb.minX, midY, midZ).next();
		
		bufferBuilder.vertex(bb.minX, midY, midZ).next();
		bufferBuilder.vertex(midX, midY, bb.minZ).next();
		
		bufferBuilder.vertex(midX, midY, bb.minZ).next();
		bufferBuilder.vertex(bb.maxX, midY, midZ).next();
		
		bufferBuilder.vertex(bb.maxX, midY, midZ).next();
		bufferBuilder.vertex(midX, midY, bb.maxZ).next();
		
		bufferBuilder.vertex(midX, bb.maxY, midZ).next();
		bufferBuilder.vertex(bb.maxX, midY, midZ).next();
		
		bufferBuilder.vertex(midX, bb.maxY, midZ).next();
		bufferBuilder.vertex(bb.minX, midY, midZ).next();
		
		bufferBuilder.vertex(midX, bb.maxY, midZ).next();
		bufferBuilder.vertex(midX, midY, bb.minZ).next();
		
		bufferBuilder.vertex(midX, bb.maxY, midZ).next();
		bufferBuilder.vertex(midX, midY, bb.maxZ).next();
		
		bufferBuilder.vertex(midX, bb.minY, midZ).next();
		bufferBuilder.vertex(bb.maxX, midY, midZ).next();
		
		bufferBuilder.vertex(midX, bb.minY, midZ).next();
		bufferBuilder.vertex(bb.minX, midY, midZ).next();
		
		bufferBuilder.vertex(midX, bb.minY, midZ).next();
		bufferBuilder.vertex(midX, midY, bb.minZ).next();
		
		bufferBuilder.vertex(midX, bb.minY, midZ).next();
		bufferBuilder.vertex(midX, midY, bb.maxZ).next();
	}
	
	public static void drawArrow(Vec3d from, Vec3d to, MatrixStack matrixStack)
	{
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		
		double startX = from.x;
		double startY = from.y;
		double startZ = from.z;
		
		double endX = to.x;
		double endY = to.y;
		double endZ = to.z;
		
		matrixStack.push();
		Matrix4f matrix = matrixStack.peek().getModel();
		
		bufferBuilder
			.vertex(matrix, (float)startX, (float)startY, (float)startZ).next();
		bufferBuilder.vertex(matrix, (float)endX, (float)endY, (float)endZ)
			.next();
		
		matrixStack.translate(endX, endY, endZ);
		matrixStack.scale(0.1F, 0.1F, 0.1F);
		
		double xDiff = endX - startX;
		double yDiff = endY - startY;
		double zDiff = endZ - startZ;
		
		float xAngle = (float)(Math.atan2(yDiff, -zDiff) + Math.toRadians(90));
		matrixStack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(xAngle));
		
		double yzDiff = Math.sqrt(yDiff * yDiff + zDiff * zDiff);
		float zAngle = (float)Math.atan2(xDiff, yzDiff);
		matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(zAngle));
		
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		
		matrixStack.pop();
		
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	
	public static void drawArrow(Vec3d from, Vec3d to,
		VertexBuffer vertexBuffer)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		
		drawArrow(from, to, bufferBuilder);
		
		bufferBuilder.end();
		vertexBuffer.upload(bufferBuilder);
	}
	
	public static void drawArrow(Vec3d from, Vec3d to,
		BufferBuilder bufferBuilder)
	{
		double startX = from.x;
		double startY = from.y;
		double startZ = from.z;
		
		double endX = to.x;
		double endY = to.y;
		double endZ = to.z;
		
		Matrix4f matrix = new Matrix4f();
		matrix.loadIdentity();
		
		bufferBuilder
			.vertex(matrix, (float)startX, (float)startY, (float)startZ).next();
		bufferBuilder.vertex(matrix, (float)endX, (float)endY, (float)endZ)
			.next();
		
		matrix.multiplyByTranslation((float)endX, (float)endY, (float)endZ);
		matrix.multiply(Matrix4f.scale(0.1F, 0.1F, 0.1F));
		
		double xDiff = endX - startX;
		double yDiff = endY - startY;
		double zDiff = endZ - startZ;
		
		float xAngle = (float)(Math.atan2(yDiff, -zDiff) + Math.toRadians(90));
		matrix.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(xAngle));
		
		double yzDiff = Math.sqrt(yDiff * yDiff + zDiff * zDiff);
		float zAngle = (float)Math.atan2(xDiff, yzDiff);
		matrix.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(zAngle));
		
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, 1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, -1, 2, 0).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, -1).next();
		
		bufferBuilder.vertex(matrix, 0, 0, 0).next();
		bufferBuilder.vertex(matrix, 0, 2, 1).next();
	}
}