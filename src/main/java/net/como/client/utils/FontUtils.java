package net.como.client.utils;

import net.como.client.ComoClient;
import net.como.client.interfaces.mixin.IClient;
import net.como.client.interfaces.mixin.IFontManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;

public class FontUtils {
    public static TextRenderer createTextRenderer(Identifier fontId) {
        IClient client = (IClient)ComoClient.getClient();
        IFontManager fontManager = (IFontManager)client.getFontManager();

        return fontManager.createTextRendererFromIdentifier(fontId);
    }

    public static TextRenderer createTextRenderer(String fontId) {
        return createTextRenderer(new Identifier(fontId));
    }
}
