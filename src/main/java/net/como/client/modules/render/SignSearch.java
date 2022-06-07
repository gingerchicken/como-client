package net.como.client.modules.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.render.RenderWorldEvent;
import net.como.client.events.render.RenderWorldViewBobbingEvent;
import net.como.client.events.render.SignRenderEvent;
import net.como.client.modules.Module;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class SignSearch extends Module {

    private final int SIGN_ROWS = 4;
    private int foundSigns = 0;

    @Override
    public String listOption() {
        return String.valueOf(this.foundSigns);
    }

    public SignSearch() {
        super("SignSearch");
        this.setDescription("Allows you to search for signs and their text.");

        this.setCategory("Render");

        this.addSetting(new Setting("SearchText", new HashMap<String, Boolean>()));
        this.addSetting(new Setting("CaseSensitive", false));

        this.addSetting(new Setting("Tracer", true));
        this.addSetting(new Setting("Box", true));
    }

    @Override
    public void activate() {
        this.addListen(SignRenderEvent.class);
        this.addListen(RenderWorldEvent.class);
        this.addListen(RenderWorldViewBobbingEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(SignRenderEvent.class);
        this.removeListen(RenderWorldEvent.class);
        this.removeListen(RenderWorldViewBobbingEvent.class);

        this.signs.clear();
    }

    public String correctCase(String str) {
        return !this.getBoolSetting("CaseSensitive") ? str.toLowerCase() : str;
    }

    public boolean isTarget(String str) {
        str = correctCase(str);

        for (String key : this.getHashMapSetting("SearchText").keySet()) {
            key = correctCase(key);
            if (str.contains(key)) return true;
        }

        return false;
    }

    private HashMap<BlockPos, SignBlockEntity> signs = new HashMap<>();

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldViewBobbingEvent": {
                if (!this.getBoolSetting("Tracer") || foundSigns == 0) break;

                RenderWorldViewBobbingEvent e = (RenderWorldViewBobbingEvent)event;
                e.cancel = true;
                

                break;
            }

            // TODO change to use a chunk builder

            case "SignRenderEvent": {
                SignRenderEvent e = (SignRenderEvent)event;
                SignBlockEntity sign = e.signBlockEntity;
                
                boolean found = false;
                for (int i = 0; i < this.SIGN_ROWS; i++) {
                    String str = sign.getTextOnRow(i, false).getString();

                    if (!this.isTarget(str)) continue;

                    found = true;
                    break;
                }

                if (!found) break;

                signs.put(sign.getPos(), sign);

                break;
            }

            case "RenderWorldEvent": {
                RenderWorldEvent e = (RenderWorldEvent)event;

                for (BlockPos pos : this.signs.keySet()) {
                    if (this.getBoolSetting("Tracer"))  RenderUtils.drawTracer(e.mStack, MathsUtils.blockPosToVec3d(pos), e.tickDelta);
                    if (this.getBoolSetting("Box"))     RenderUtils.renderBlockBox(e.mStack, pos);
                }

                foundSigns = this.signs.size();
                this.signs.clear();

                break;
            }
        }
    }
}
