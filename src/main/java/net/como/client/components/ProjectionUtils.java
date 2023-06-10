package net.como.client.components;

import com.mojang.blaze3d.systems.RenderSystem;

import net.como.client.ComoClient;
import net.como.client.misc.maths.Vec3;
import net.como.client.utils.MathsUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import net.minecraft.util.math.Vec3d;

public class ProjectionUtils {
    private final Vector4f vec4 = new Vector4f();
    private final Vector4f mmMat4 = new Vector4f();
    private final Vector4f pmMat4 = new Vector4f();
    private final Vec3 camera = new Vec3();

    private final Vec3 cameraNegated = new Vec3();

    private Matrix4f model;
    private Matrix4f projection;

    private double windowScale;

    private static ProjectionUtils instance = null;

    public static ProjectionUtils getInstance() {
        if (instance == null) {
            instance = new ProjectionUtils();
        }

        return instance;
    }

    public void update(MatrixStack matrices, Matrix4f projection) {
        MinecraftClient mc = ComoClient.getClient();

        model = new Matrix4f(matrices.peek().getPositionMatrix());
        this.projection = projection;

        camera.set(mc.gameRenderer.getCamera().getPos());
        cameraNegated.set(camera);
        cameraNegated.negate();

        windowScale = mc.getWindow().calculateScaleFactor(1, mc.forcesUnicodeFont());
    }

    public boolean to2D(Vec3 pos, double scale) {
        MinecraftClient mc = ComoClient.getClient();

        vec4.set(cameraNegated.x + pos.x, cameraNegated.y + pos.y, cameraNegated.z + pos.z, 1);

        vec4.mul(model, mmMat4);
        mmMat4.mul(projection, pmMat4);

        if (pmMat4.w <= 0.0f) return false;

        toScreen(pmMat4);
        double x = pmMat4.x * mc.getWindow().getFramebufferWidth();
        double y = pmMat4.y * mc.getWindow().getFramebufferHeight();

        if (Double.isInfinite(x) || Double.isInfinite(y)) return false;

        pos.set(x / windowScale / scale, (mc.getWindow().getFramebufferHeight() - y / windowScale) / scale, pmMat4.z);
        return true;
    }

    public static double getScale(Vec3d pos, float tickDelta) {
        double dist = ComoClient.getClient().cameraEntity.getLerpedPos(tickDelta).distanceTo(pos);

        return MathsUtils.clamp(
            1 - dist / 100,
            0.5,
            Double.MAX_VALUE
        );
    }

    public static double getScale(Vec3 pos, float tickDelta) {
        return getScale(pos.to3d(), tickDelta);
    }

    public static void unscaledProjection() {
        MinecraftClient mc = ComoClient.getClient();

        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), 0, 1000, 3000));
    }

    public static void scaleProjection(float scale) {
        MinecraftClient mc = ComoClient.getClient();

        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, (float) (mc.getWindow().getFramebufferWidth() / mc.getWindow().getScaleFactor()), (float) (mc.getWindow().getFramebufferHeight() / mc.getWindow().getScaleFactor()), 0, 1000, 3000));
    }

    public static void resetProjection() {
        scaleProjection((float)ComoClient.getClient().getWindow().getScaleFactor());
    }

    private static void toScreen(Vector4f vec) {
        float newW = 1.0f / vec.w * 0.5f;

        vec.x = vec.x * newW + 0.5f;
        vec.y = vec.y * newW + 0.5f;
        vec.z = vec.z * newW + 0.5f;
        vec.w = newW;
    }
}
