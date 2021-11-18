package net.como.client.cheats;

import java.util.HashMap;
import java.util.List;

import net.como.client.CheatClient;
import net.como.client.events.RenderWorldEvent;
import net.como.client.interfaces.mixin.IWorld;
import net.como.client.structures.Cheat;
import net.como.client.structures.Colour;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.RenderUtils;
import net.minecraft.world.chunk.BlockEntityTickInvoker;

public class BlockESP extends Cheat {
    // Currently for ticker entities only!
    // TODO make it work for all blocks

    public BlockESP() {
        super("BlockESP");

        this.addSetting(new Setting("Blocks", new HashMap<String, Boolean>()));
    
        this.description = "Makes specific blocks visible through walls.";
    }

    @Override
    public void activate() {
        this.addListen(RenderWorldEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderWorldEvent.class);
    }

    @SuppressWarnings("unchecked")
    private boolean shouldRender(BlockEntityTickInvoker ticker) {
        return ((HashMap<String, Boolean>)(this.getSetting("Blocks").value)).containsKey(ticker.getName());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldEvent": {
                RenderWorldEvent e = (RenderWorldEvent)event;

                Colour colour = CheatClient.config.storageColour;

                List<BlockEntityTickInvoker> tickers = ((IWorld)(CheatClient.getClient().world)).getBlockEntityTickers();
                for (BlockEntityTickInvoker ticker : tickers) {
                    if (!this.shouldRender(ticker)) continue;
                    RenderUtils.renderBlockBox(e.mStack, ticker.getPos(), colour.r, colour.g, colour.b, colour.a);
                }

                break;
            }
        }
    }
}
