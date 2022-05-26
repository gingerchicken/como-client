package net.como.client.modules.hud;

import com.mojang.blaze3d.systems.RenderSystem;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.client.OnAttackEntityEvent;
import net.como.client.events.packet.OnGameStateChangeEvent;
import net.como.client.events.render.InGameHudRenderEvent;
import net.como.client.modules.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Hitmarker extends Module {
    public static final Identifier HITMARKER_TEXTURE = new Identifier("como-client", "textures/gui/misc/hitmarker.png");
    public static final Identifier HITMARKER_SOUND = new Identifier("como-client:hitmarker");
    
    public static final SoundEvent HITMARKER_SOUND_EVENT = new SoundEvent(HITMARKER_SOUND);

    public static final int HITMARKER_WIDTH  = 32;
    public static final int HITMARKER_HEIGHT = 32;

    public Hitmarker() {
        super("Hitmarker");

        this.setDescription("Show when you hit someone with a melee weapon");

        this.setCategory("HUD");

        this.addSetting(new Setting("AlphaStep", 255d));
        this.addSetting(new Setting("HoldTicks", 0));
        this.addSetting(new Setting("AnimOpen", false));
        this.addSetting(new Setting("AnimClose", false));

        this.addSetting(new Setting("MaxDelay", 1d));
        this.addSetting(new Setting("Scale", 0.25d));

        this.addSetting(new Setting("Sound", true));
    }
    
    @Override
    public void activate() {
        this.addListen(OnAttackEntityEvent.class);
        this.addListen(ClientTickEvent.class);
        this.addListen(InGameHudRenderEvent.class);
        this.addListen(OnGameStateChangeEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnGameStateChangeEvent.class);
        this.removeListen(OnAttackEntityEvent.class);
        this.removeListen(ClientTickEvent.class);
        this.removeListen(InGameHudRenderEvent.class);
    }

    public void render(MatrixStack matrixStack, float partialTicks) {
        RenderSystem.setShaderTexture(0, HITMARKER_TEXTURE);
        float k = 0.75f;
        RenderSystem.setShaderColor(k, k, k, k);

        double scale = this.getDoubleSetting("Scale") + alpha/(255d*8d);

        // Scaling
        int width  = (int)(HITMARKER_WIDTH * scale);
        int height = (int)(HITMARKER_HEIGHT * scale);

        int offsetX = 0;
        int offsetY = 0;

        int x = ComoClient.getClient().getWindow().getScaledWidth() / 2 - width / 2 + offsetX;
        int y = ComoClient.getClient().getWindow().getScaledHeight() / 2 - height / 2 + offsetY;

        DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, width, height, width, height);
    }

    public void doHitmarker() {
        this.resetHit();

        this.shouldDrawHitmarker = true;
        this.alpha = 0f;
        this.heldTicks = 0;
        this.hitmarkerState = this.getBoolSetting("AnimOpen") ? STATE.OPENING : STATE.HOLD;

        if (this.getBoolSetting("Sound")) ComoClient.me().playSound(HITMARKER_SOUND_EVENT, 1, 1);
    }

    public static enum STATE {
        OPENING,
        HOLD,
        CLOSING
    };

    private STATE nextState() {
        switch (this.hitmarkerState) {
            case OPENING: return STATE.HOLD;
            case HOLD: {
                if (!this.getBoolSetting("AnimClose")) return STATE.OPENING;

                return STATE.CLOSING;
            }
            case CLOSING: return STATE.OPENING;
            default: return null;
        }
    }

    private STATE hitmarkerState = STATE.OPENING;

    private float alpha = 0f;
    private int heldTicks = 0;

    private void incAlpha() {
        float next = alpha + (float)(double)this.getDoubleSetting("AlphaStep");
        alpha = next > 255f ? 255f : next;
    }

    private void decAlpha() {
        float next = alpha - (float)(double)this.getDoubleSetting("AlphaStep");
        alpha = next < 0 ? 0 : next;
    }

    public void hitmarkerTick() {
        switch (this.hitmarkerState) {
            case OPENING: {
                if (alpha >= 255f || !this.getBoolSetting("AnimOpen")) {
                    this.hitmarkerState = nextState();
                    this.alpha = 255f;
                    break;
                }

                this.incAlpha();
                break;
            }

            case HOLD: {
                alpha = 255f;
                if (this.heldTicks >= this.getIntSetting("HoldTicks")) {
                    if (!this.getBoolSetting("AnimClose")) {
                        this.shouldDrawHitmarker = false;
                    }

                    this.hitmarkerState = nextState();
                }

                this.heldTicks++;

                break;
            }

            case CLOSING: {
                if (alpha <= 0f || !this.getBoolSetting("AnimClose")) {
                    this.hitmarkerState = nextState();
                    this.shouldDrawHitmarker = false; // Done!
                    break;
                }

                decAlpha();
                break;
            }
        }
    }

    private Boolean shouldDrawHitmarker = false;


    private LivingEntity target;
    private float beforeHealth;
    private double hitTime;
    private void registerHit(LivingEntity entity) {
        this.target = entity;
        this.beforeHealth = entity.getHealth();
        this.hitTime = ComoClient.getCurrentTime();
    }

    private void resetHit() {
        this.target = null;
    }

    int arrowHits = 0;
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                if (!this.shouldDrawHitmarker) break;

                this.render(e.mStack, e.tickDelta);
                
                break;
            }

            case "ClientTickEvent": {
                this.arrowHits = 0;

                if (this.shouldDrawHitmarker) {
                    this.hitmarkerTick();
                }

                if (this.target == null) break;
                if (ComoClient.getCurrentTime() > this.hitTime + this.getDoubleSetting("MaxDelay")) {
                    this.resetHit();

                    break;
                }

                if (this.target.getHealth() < this.beforeHealth) {
                    this.doHitmarker();
                    break;
                }
                
                break;
            }

            case "OnGameStateChangeEvent": {
                OnGameStateChangeEvent e = (OnGameStateChangeEvent)event;

                GameStateChangeS2CPacket packet = e.packet;
                
                // Make sure that it is a projectile hit event
                if (packet.getReason() != GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER) break;

                if (this.arrowHits % 2 == 0) this.doHitmarker();
                this.arrowHits++;

                break;
            }

            case "OnAttackEntityEvent": {
                OnAttackEntityEvent e = (OnAttackEntityEvent)event;

                if (e.player != ComoClient.me() || !(e.target instanceof LivingEntity)) break;

                this.registerHit((LivingEntity)e.target);
                
                break;
            }
        }
    }
}
