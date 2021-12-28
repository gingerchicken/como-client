package net.como.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

public class Widget {
    private Vec2f pos;
    private Vec2f size;
    private boolean clickable;

    public Widget(Vec2f position, Vec2f size) {
        this.setPosition(position);
        this.setSize(size);
    }

    private Widget parentWidget;
    private List<Widget> children = new ArrayList<>();

    public boolean hasChild() {
        return this.getChildren().size() == 0;
    }

    public List<Widget> getChildren() {
        return this.children;
    }

    protected void addChild(Widget child) {
        this.children.add(child);
        child.parentWidget = this;
    }

    public void setParent(Widget parent) {
        parent.addChild(this);
    }

    public Widget getParent() {
        return this.parentWidget;
    }

    public Vec2f getPosition() {
        return this.pos;
    }
    public void setPosition(Vec2f pos) {
        this.pos = pos;
    }
    public Vec2f getScreenPosition() {
        if (!this.isChild()) return this.getPosition();

        return new Vec2f(
            this.getPosition().x + this.getParent().getScreenPosition().x,
            this.getPosition().y + this.getParent().getScreenPosition().y
        );
    }

    public boolean isClickable() {
        return this.clickable;
    }
    public boolean isChild() {
        return this.parentWidget != null;
    }

    public Vec2f getRightPosition() {
        return new Vec2f(
            this.getPosition().x + this.getSize().x,
            this.getPosition().y + this.getSize().y
        );
    }

    public Vec2f getRightScreenPosition() {
        return new Vec2f(
            this.getScreenPosition().x + this.getSize().x,
            this.getScreenPosition().y + this.getSize().y
        );
    }

    public Vec2f getSize() {
        return this.size;
    }
    public void setSize(Vec2f size) {
        this.size = size;
    }

    public void render(MatrixStack matrixStack) {}
    public void clicked() {}
}
