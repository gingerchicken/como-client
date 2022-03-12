package net.como.client.interfaces.mixin;

import net.minecraft.client.font.FontManager;

public interface IClient {
    public FontManager getFontManager();
    public void performItemUse();
    public boolean performAttack();
    public void setAttackCooldown(int cooldown);
}
