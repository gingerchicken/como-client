package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.events.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class RenderHealthBarEvent extends Event {
    public DrawContext context;
    public PlayerEntity player;
    public int x;
    public int y;
    public int lines;
    public int regeneratingHeartIndex;
    public float maxHealth;
    public int lastHealth;
    public int health;
    public int absorption;
    public boolean blinking;
    public CallbackInfo ci;

    public RenderHealthBarEvent(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        this.context = context;
        this.player = player;
        this.x = x;
        this.y = y;
        this.lines = lines;
        this.regeneratingHeartIndex = regeneratingHeartIndex;
        this.maxHealth = maxHealth;
        this.lastHealth = lastHealth;
        this.health = health;
        this.absorption = absorption;
        this.blinking = blinking;
        this.ci = ci;
    }
}
