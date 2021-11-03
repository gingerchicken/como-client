package net.como.client.structures;

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

    }
}
