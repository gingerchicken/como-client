package net.como.client.misc.attributes.player;

import net.como.client.ComoClient;
import net.como.client.misc.attributes.PlayerAttribute;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PingAttribute extends PlayerAttribute {
    public PingAttribute(PlayerEntity player) {
        super(player);
    }

    private Integer getPing() {
        ClientPlayNetworkHandler lv = ComoClient.me().networkHandler;

        // Get the player entry
        PlayerListEntry entry = lv.getPlayerListEntry(this.getPlayer().getUuid());
        
        // We don't know em so they must be apart of the server right?
        if (entry == null) return 0;

        return entry.getLatency();
    }

    @Override
    public Text getText() {
        return Text.of(String.format("%dms", this.getPing()));
    }

    @Override
    public int getColour() {
        Integer ping = this.getPing(); 

        int badPing = 500;
        float f = (float)ping/(float)badPing * 255;
        if (f > 255) f = 255;

        return RenderUtils.RGBA2Int((int)(2*f), (int)(255 - f), 0, 255);
    } 
}
