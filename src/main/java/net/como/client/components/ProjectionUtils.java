package net.como.client.components;

import net.como.client.ComoClient;
import net.como.client.interfaces.mixin.IMatrix4f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;

public class ProjectionUtils {
    private static Vector4f vec4 = new Vector4f();
    private static Vector4f mmMat4 = new Vector4f();
    private static Vector4f pmMat4 = new Vector4f();

    private static Vec3d camera = Vec3d.ZERO;
    private static Vec3d cameraNegated = Vec3d.ZERO;

    private static Matrix4f model;
    private static Matrix4f projection;
    private static double windowScale;

    public static void update(MatrixStack matrixStack, Matrix4f matrix4f) {
        model = matrixStack.peek().getPositionMatrix().copy();
        projection = matrix4f;

        MinecraftClient client = ComoClient.getClient();

        camera = client.gameRenderer.getCamera().getPos();
        cameraNegated = camera.negate();

        windowScale = client.getWindow().calculateScaleFactor(1, client.forcesUnicodeFont());
    }

    public double getScale(Vec3d pos, float partialTicks) {
        return Math.sqrt(ComoClient.getClient().cameraEntity.getLerpedPos(partialTicks).distanceTo(pos));
    }

    public static final class Projection2D {
        public Vec3d pos;
        public final boolean shouldRender;

        public Projection2D(Vec3d pos, boolean shouldRender) {
            this.pos = pos;
            this.shouldRender = shouldRender;
        }

        public static final Projection2D NO_RENDER = new Projection2D(Vec3d.ZERO, false);
    }

    public static Projection2D to2D(Vec3d pos, double scale) {
        vec4.set((float)(cameraNegated.x + pos.x), (float)(cameraNegated.y + pos.y), (float)(cameraNegated.z + pos.z), 1);

        mmMat4 = ((IMatrix4f)(Object)(model)).multiplyMatrix(vec4);
        pmMat4 = ((IMatrix4f)(Object)(projection)).multiplyMatrix(vec4);

        if (pmMat4.getW() <= 0.0f) return Projection2D.NO_RENDER;

        pmMat4 = toScreen(pmMat4);

        MinecraftClient client = ComoClient.getClient();
        Window window = client.getWindow();

        float x = pmMat4.getX() * window.getFramebufferWidth();
        float y = pmMat4.getY() * window.getFramebufferHeight();

        if (Float.isInfinite(x) || Double.isInfinite(y)) return Projection2D.NO_RENDER;

        return new Projection2D(
            new Vec3d(
                x / windowScale,
                window.getFramebufferHeight() - (y / windowScale), // Minecraft moment
                pmMat4.getZ()
            ), true
        );
    }

    private static Vector4f toScreen(Vector4f v) {
        float newW = 1.0f / v.getW() * 0.5f;

        return new Vector4f(
            v.getX() * newW + 0.5f,
            v.getY() * newW + 0.5f,
            v.getZ() * newW + 0.5f,
            newW
        );
    }
}
