package net.como.client.misc;

public class GUIPos {
    public static enum Type {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    // Position
    private Type pos;
    public Type getPos() {
        return this.pos;
    }
    public void setPos(Type pos) {
        this.pos = pos;
    }

    // Constructors
    public GUIPos(Type pos) {
        this.pos = pos;
    }

    public static GUIPos fromInt(int i) {
        return new GUIPos(getType(i));
    }

    public static GUIPos fromInt(int i, Type defaultType) {
        return new GUIPos(getType(i, defaultType));
    }

    private static Type getType(int i) {
        return getType(i, Type.TOP_LEFT);
    }

    private static Type getType(int i, Type defaultType) {
        switch (i) {
            case 1:     return Type.TOP_LEFT;
            case 2:     return Type.TOP_RIGHT;
            case 3:     return Type.BOTTOM_LEFT;
            case 4:     return Type.BOTTOM_RIGHT;

            default:    return defaultType;
        }
    }

    public boolean isRight() {
        return !this.isLeft();
    }
    public boolean isTop() {
        return !this.isBottom();
    }
    public boolean isLeft() {
        switch (this.getPos()) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return true;
            default:
                return false;
        }
    }
    public boolean isBottom() {
        switch (this.getPos()) {
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return true;
            default:
                return false;
        }
    }
}
