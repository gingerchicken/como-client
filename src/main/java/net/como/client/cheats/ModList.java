package net.como.client.cheats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.font.TextRenderer;
import net.como.client.CheatClient;
import net.como.client.structures.Cheat;

public class ModList extends Cheat {

    public ModList() {
        super("ModList");
    }
    
    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "InGameHubRender": {
                MatrixStack mStack = (MatrixStack)args[0];
                float tickDelta = (float)args[1];
                CallbackInfo ci = (CallbackInfo)args[2];

                TextRenderer textRenderer = CheatClient.getClient().textRenderer;

                List<Cheat> enabledMods = new ArrayList<Cheat>();
                
                for (String cheatName : CheatClient.Cheats.keySet()) {
                    Cheat cheat = CheatClient.Cheats.get(cheatName);

                    if (!cheat.isEnabled()) continue;
                    
                    enabledMods.add(cheat);
                }

                // Sort the enabledMods list by the cheat name
                Collections.sort(enabledMods, (c1, c2) -> {
                    return c2.getName().length() - c1.getName().length();
                });

                int display = 0;
                for (Cheat cheat : enabledMods) {
                    textRenderer.drawWithShadow(mStack, cheat.getName(), 1, 1+10*display, 0xFFFFFFFF);
                    display++;
                }
            }
        }
    }
}
