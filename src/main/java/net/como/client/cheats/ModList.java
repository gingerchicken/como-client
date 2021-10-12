package net.como.client.cheats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.font.TextRenderer;
import net.como.client.CheatClient;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;

public class ModList extends Cheat {
    private static interface ColouringMode {
        int getColour(int cur, int total);
    }

    private static class DefaultColouring implements ColouringMode {
        protected List<Integer> flat;

        public DefaultColouring() {
            this.flat = Arrays.asList(new Integer[]{
                0xffffffff
            });
        }

        @Override
        public int getColour(int cur, int total) {
            int index = (int)((float)flat.size() * ((float)cur/(float)total));
            return this.flat.get(index);
        }
    }
    private static class TransColouring extends DefaultColouring {
        public TransColouring() {
            this.flat = Arrays.asList(new Integer[]{
                0xff55cdfc,
                0xfff7a8b8,
                0xffffffff,
                0xfff7a8b8,
                0xff55cdfc
            });
        }
    }

    private HashMap<String, ColouringMode> colouringModes;

    public ModList() {
        super("ModList");
        
        this.description = "Displays all of your enabled mods";
        this.modListDisplay = false;

        this.addSetting(new Setting("ColouringMode", "default"));

        // Setup colouring modes
        colouringModes = new HashMap<String, ColouringMode>() {{
            put("default", new DefaultColouring());
            put("trans", new TransColouring());
        }};
    }

    private ColouringMode getColouringMode() {
        String mode = (String)this.getSetting("ColouringMode").value;
        mode = mode.toLowerCase();
        
        // If the mode doesn't exist return to default.
        mode = (this.colouringModes.containsKey(mode)) ? mode : "default";

        // Return the mode.
        return this.colouringModes.get(mode);
    }

    @Override
    public void activate() {
        this.addListen(InGameHudRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InGameHudRenderEvent": {
                InGameHudRenderEvent e = (InGameHudRenderEvent)event;

                TextRenderer textRenderer = CheatClient.getClient().textRenderer;
                List<Cheat> enabledMods = new ArrayList<Cheat>();
                
                for (String cheatName : CheatClient.Cheats.keySet()) {
                    Cheat cheat = CheatClient.Cheats.get(cheatName);

                    if (!cheat.shouldDisplayInModList()) continue;
                    
                    enabledMods.add(cheat);
                }

                // Sort the enabledMods list by the cheat name
                Collections.sort(enabledMods, (c1, c2) -> {
                    int l1 = textRenderer.getWidth(c1.getName());
                    int l2 = textRenderer.getWidth(c2.getName());

                    l1 += c1.hasListOption() ? textRenderer.getWidth(c1.listOption()) + 1 : 0;
                    l2 += c2.hasListOption() ? textRenderer.getWidth(c2.listOption()) + 1 : 0;

                    return l2 - l1;
                });

                int display = 0;
                for (Cheat cheat : enabledMods) {
                    int x = textRenderer.drawWithShadow(e.mStack, cheat.getName(), 1, 1+10*display, this.getColouringMode().getColour(display, enabledMods.size()));

                    if (cheat.hasListOption()) {
                        textRenderer.drawWithShadow(e.mStack, String.format("[%s]", cheat.listOption()), x+2, 1+10*display, 0xFFadadad);
                    }                    
                    
                    display++;
                }
            }
        } 
    }
}
