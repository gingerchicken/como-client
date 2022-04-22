package net.como.client.events;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.events.Event;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class RenderHealthBarEvent extends Event {
    public MatrixStack matrices;
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

    public RenderHealthBarEvent(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        this.matrices = matrices;
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
