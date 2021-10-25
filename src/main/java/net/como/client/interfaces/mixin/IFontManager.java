package net.como.client.interfaces.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;

public interface IFontManager {
    public TextRenderer createTextRendererFromIdentifier(Identifier id);
}
