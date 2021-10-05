package net.como.client.cheats;

import java.util.HashMap;
import java.util.List;

import net.como.client.CheatClient;
import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderWorldViewBobbingEvent;
import net.como.client.interfaces.mixin.IWorld;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.MathsUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.world.chunk.BlockEntityTickInvoker;

public class Tracers extends Cheat {

    public Tracers() {
        super("Tracers");

        this.addSetting(new Setting("Player", true));
        this.addSetting(new Setting("Mob", true));
        this.addSetting(new Setting("Item", true));
        this.addSetting(new Setting("OtherEntities", true));
        
        // TODO add block search
        // TODO add entity search mode

        // Block/Ticker search
        this.addSetting(new Setting("Search", false));
        this.addSetting(new Setting("SearchPhrases", new HashMap<String, Boolean>()));
    }
    
    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(RenderWorldViewBobbingEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(RenderWorldViewBobbingEvent.class);
    }

    public boolean shouldDrawTracer(Entity ent) {
        boolean drawPlayer        = (boolean)this.getSetting("Player").value;
        boolean drawMob           = (boolean)this.getSetting("Mob").value;
        boolean drawItem          = (boolean)this.getSetting("Item").value;
        boolean drawOtherEntities = (boolean)this.getSetting("OtherEntities").value;

        boolean isMob       = ent instanceof MobEntity;
        boolean isPlayer    = ent instanceof PlayerEntity;
        boolean isItem      = ent instanceof ItemEntity;

        return (
                (drawPlayer && isPlayer)
            ||  (drawMob && isMob)
            ||  (drawItem && isItem)
            ||  (drawOtherEntities && !isMob && !isPlayer && !isItem)
        );
    }

    @SuppressWarnings("unchecked")
    public boolean shouldDrawTracer(BlockEntityTickInvoker block) {
        HashMap<String, Boolean> phrases = (HashMap<String, Boolean>)this.getSetting("SearchPhrases").value;

        return phrases.containsKey(block.getName());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldViewBobbingEvent": {
                RenderWorldViewBobbingEvent e = (RenderWorldViewBobbingEvent)event;
                e.cancel = true;
                

                break;
            }

            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;

                // Render entity/player tracers
                Iterable<Entity> ents = CheatClient.getClient().world.getEntities();
                for (Entity entity : ents) {
                    if (!this.shouldDrawTracer(entity)) continue;

                    // No render myself.
                    if (entity instanceof PlayerEntity && (PlayerEntity)entity == CheatClient.me()) {
                        continue;
                    }

                    // Render tracers
                    RenderUtils.drawTracer(e.mStack, MathsUtils.getLerpedCentre(entity, e.tickDelta), e.tickDelta);
                }

                // Render chests etc.
                List<BlockEntityTickInvoker> tickers = ((IWorld)(CheatClient.getClient().world)).getBlockEntityTickers();
                for (BlockEntityTickInvoker ticker : tickers) {
                    if (!this.shouldDrawTracer(ticker)) continue;

                    RenderUtils.drawTracer(e.mStack, BlockUtils.blockPos(ticker.getPos()).add(0.5, 0.5, 0.5), e.tickDelta);
                }

                break;
            }
        }
    }
}
