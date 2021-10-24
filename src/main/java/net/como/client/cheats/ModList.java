package net.como.client.cheats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.InGameHudRenderEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.Colour;
import net.como.client.structures.GUIPos;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.RenderUtils;

public class ModList extends Cheat {
    private GUIPos getPosition() {
        return GUIPos.fromInt(
            (int)this.getSetting("Positioning").value,
            GUIPos.Type.TOP_LEFT
        );
    }

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
    private static class LGBTColouring extends DefaultColouring {
        public LGBTColouring() {
            this.flat = Arrays.asList(new Integer[]{
                0xffe40303,
                0xffff8c00,
                0xffffed00,
                0xff008026,
                0xff004dff,
                0xff750787
            });
        }
    }
    private static class RGBColouring implements ColouringMode {
        public RGBColouring() {
            // Generate all of the steps, it is hard work but at least we only need to do it once.
            this.generateSteps();

            // We need to run a tick so we can generate at least one set.
            this.tick();
        }

        private Cheat getModList() {
            return CheatClient.Cheats.get("modlist");
        }

        private int getSpeed() {
            return (int)this.getModList().getSetting("RGBIntensity").value;
        }

        // This is used to say where the head of the function is (i.e. 0)
        private int head = 0;
        private void generateSteps() {
            // The 763 is the period of the "step" function (i.e. the thing generating all of the colours).
            this.steps = new Colour[763];

            // This is just from an RGB thing I found on a site this https://codepen.io/Codepixl/pen/ogWWaK
            // Thanks <3

            // The "steps" function
            int r = 255, g = 0, b = 0;
            for (int i = 0; i < this.steps.length; i++) {
                if (r > 0 && b == 0){
                    r--;
                    g++;
                }
                if (g > 0 && r == 0){
                    g--;
                    b++;
                }
                if (b > 0 && g == 0){
                    r++;
                    b--;
                }

                // Place the new colour that we generated in the steps array.
                this.steps[i] = new Colour(r, g, b, 255);
            }
        }
        
        // This is used to store all of the possible colours
        private Colour[] steps;
        
        // This is used to store or of the possible colours for all of the cheats for the active tick.
        private List<Colour> set;

        // This will prepare all of the colours so we can tick over them all at a constant rate (i.e. client TPS)
        private void tick() {
            set = new ArrayList<Colour>();
            int totalCheats = CheatClient.Cheats.keySet().size();
            int speed = this.getSpeed();

            // Loop through them all as if we are wanting them their and then.
            for (int cur = 0; cur < totalCheats; cur++) {
                int len = this.steps.length;

                // If the cur == 0 then that must mean that it is the head.
                if (cur == 0) {
                    // This adds one to head
                    head += speed;

                    // Technically this does mean that head will infinitely increase, however for it to be a problem the user needs to be on MC for a super long time, which all it would mean is that the int would wrap round to the negatives.
                    // ...which would cause an error as it would be looking for a negative index in the set array... so I guess I will fix this...
                    // So let's check for that
                    head = head < 0 ? speed : head; 
                }
                
                // Get the element behind the head.
                int i = head - cur*speed;

                // If it is less than zero then loop it around the array, else then just leave it be.
                i = (i < 0 ? len - i : i);

                // Add the step to the set
                // ...making sure that i is less than len.
                set.add(this.steps[i % len]);
            }
        }

        // Return a colour for a given cheat.
        @Override
        public int getColour(int cur, int total) {
            // Get the colour that would have been generated for this index
            Colour c = this.set.get(cur);

            // Return it as an integer
            return RenderUtils.RGBA2Int((int)c.r, (int)c.g, (int)c.b, 255);
        }
    }

    private HashMap<String, ColouringMode> colouringModes;

    public ModList() {
        super("ModList");
        
        this.description = "Displays all of your enabled mods";
        this.modListDisplay = false;

        this.addSetting(new Setting("ColouringMode", "default"));
        this.addSetting(new Setting("Scale", 1.0f));
        this.addSetting(new Setting("RGBIntensity", 5));
        this.addSetting(new Setting("Positioning", 1)); // TODO change this after implementing issue #44
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
        // Setup colouring modes
        colouringModes = new HashMap<String, ColouringMode>() {{
            put("default", new DefaultColouring());
            put("trans", new TransColouring());
            put("rgb", new RGBColouring());
            put("lgbt", new LGBTColouring());
        }};

        this.addListen(InGameHudRenderEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InGameHudRenderEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ColouringMode mode = this.getColouringMode();
                if (!(mode instanceof RGBColouring)) break;

                RGBColouring rgbMode = (RGBColouring)mode;
                // Generate our current set.
                rgbMode.tick();

                break;
            }
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
                    return c2.getTextWidth(textRenderer) - c1.getTextWidth(textRenderer);
                });

                Float scale = (Float)this.getSetting("Scale").value;

                e.mStack.push();
                e.mStack.scale(scale, scale, 0);

                int display = 0;

                // Positioning stuff
                GUIPos pos = this.getPosition();

                // Window sizes
                Window window = CheatClient.getClient().getWindow();
                int MAX_WIDTH  = window.getScaledWidth();
                int MAX_HEIGHT = window.getScaledHeight();

                for (Cheat cheat : enabledMods) {
                    // Positioning based stuff
                    int x = pos.isRight() ? MAX_WIDTH - cheat.getTextWidth(textRenderer) : 0;
                    
                    int y = 1+10*(display + (pos.isBottom() ? 1 : 0));
                    y = pos.isBottom() ? MAX_HEIGHT - y : y;

                    x = textRenderer.drawWithShadow(e.mStack, cheat.getName(), x, y, this.getColouringMode().getColour(display, enabledMods.size()));

                    if (cheat.hasListOption()) {
                        // The +2 is just for the space
                        textRenderer.drawWithShadow(e.mStack, String.format("[%s]", cheat.listOption()), x + 2, y, 0xFFadadad);
                    }                    
                    
                    display++;
                }

                e.mStack.pop();
            }
        } 
    }
}
