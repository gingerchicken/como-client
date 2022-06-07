package net.como.client.misc;

import net.como.client.ComoClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ColorHelper;

public class Colour {
    public float r,g,b,a;

    public static Colour GREEN = new Colour(0, 255, 0, 255);

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

    public Colour(float r, float g, float b) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = 255;
    }

    public int toARGB() {
        return ColorHelper.Argb.getArgb((int)this.a, (int)this.r, (int)this.g, (int)this.b);
    }

    public static Colour fromDistance(float distance) {
        float f = distance / 20F;
        return new Colour((2 - f) * 255, f*255, 0, 255/2);
    }

    public static Colour fromHealth(LivingEntity entity) {
        float f = (entity.getHealth() / entity.getMaxHealth()) * 255*2;
        
        return new Colour((255*2 - f), (f), 0, 255);
    }

    public static Colour fromDistance(Entity entity) {
        return fromDistance(ComoClient.me().distanceTo(entity));
    }
}
