package net.como.client.structures;

import net.como.client.ComoClient;
import net.minecraft.entity.Entity;

public class Colour {
    public float r,g,b,a;

    private static float clamp(float x) {
        if (x > 255) return 255.0f;
        if (x < 0) return 0f;

        return x;
    }

    public Colour(float r, float g, float b, float a) {

        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = clamp(a);
    }

    public static Colour fromDistance(float distance) {
        float f = distance / 20F;
        return new Colour((2 - f) * 255, f*255, 0, 255/2);
    }

    public static Colour fromDistance(Entity entity) {
        return fromDistance(ComoClient.me().distanceTo(entity));
    }
}
