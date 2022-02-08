package net.como.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.como.client.utils.ClientUtils;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.math.Vec2f;

public abstract class Widget implements Drawable {
    private Vec2f pos;
    private Vec2f size;
    protected boolean clickable;
    private float scaleFactor = 1.0f;

    public Widget popChild() {
        Widget widget = this.getChildren().get(0);
        this.getChildren().remove(0);
        
        return widget;
    }

    public void setScaleFactor(float factor) {
        if (this.isChild()) {
            this.parentWidget.setScaleFactor(factor);
            return;
        }

        this.scaleFactor = factor;
    }

    public Widget(Vec2f position, Vec2f size) {
        this.setPosition(position);
        this.setSize(size);
    }

    private Widget parentWidget;
    private List<Widget> children = new ArrayList<>();

    public boolean hasChild() {
        return this.getChildren().size() > 0;
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
        float scale = this.getScaleFactor();

        return new Vec2f(this.size.x * scale, this.size.y * scale);
    }
    public void setSize(Vec2f size) {
        this.size = size;
    }

    public boolean isInBounds(int x, int y) {
        return (x >= this.getScreenPosition().x && x <= this.getRightScreenPosition().x) && (y >= this.getScreenPosition().y && y <= this.getRightScreenPosition().y);
    }

    public boolean isInBounds(Vec2f vec) {
        return this.isInBounds((int)vec.x, (int)vec.y);
    }
    
    public boolean isMouseOver() {
        return this.isInBounds(ClientUtils.getMousePosition());
    }

    public void clicked() {
        if (!this.isClickable() || !this.hasChild()) return;

        for (Widget widget : this.getChildren()) {
            if (widget.isMouseOver()) {
                widget.clicked();
                break;
            }
        }
    }

    public float getScaleFactor() {
        if (this.isChild()) return this.parentWidget.getScaleFactor();

        return this.scaleFactor;
    }

    public void tick() {
        for (Widget widget : this.getChildren()) {
            widget.tick();
        }
    }
}
