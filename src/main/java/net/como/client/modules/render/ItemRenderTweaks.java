package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.render.RenderItemEvent;
import net.como.client.modules.Module;
import net.minecraft.util.math.Vec3d;

public class ItemRenderTweaks extends Module {
    private class HandSetting extends Setting {
        protected boolean isLeftHand() {
            return name.toLowerCase().charAt(0) == 'l';
        }

        protected boolean isRightHand() {
            return name.toLowerCase().charAt(0) == 'r';
        }

        protected String getComponent() {
            return name.substring(name.length() - 1);
        }

        public HandSetting(String name, Object defaultValue) {
            super(name, defaultValue);
        
            this.setCategory(
                this.isLeftHand() ? "Left Hand" : "Right Hand"
            );
        }

        @Override
        public boolean shouldShow() {
            return (this.isLeftHand() && getBoolSetting("LeftHand")) || (this.isRightHand() && getBoolSetting("RightHand"));
        }

        @Override
        public String getNiceName() {
            // Remove the 'l' or 'r' from the name
            return super.getNiceName().substring(1);
        }
    }
    private class OffsetSetting extends HandSetting {
        public OffsetSetting(String name, Object defaultValue) {
            super(name, defaultValue);

            this.setMin(-15d);
            this.setMax(15d);
        
            this.setDescription("Offset the " + this.getComponent());
        }
    }
    private class ScaleSetting extends HandSetting {
        public ScaleSetting(String name, Object defaultValue) {
            super(name, defaultValue);

            this.setMin(0d);
            this.setMax(5d);

            this.setDescription("Scale the " + this.getComponent());
        }
    }

    private void createHandSettings(String hand) {
        // Hand
        this.addSetting(new Setting(hand + "Hand", true) {{
            this.setDescription("Enable " + hand + " hand tweaks");
        }});

        // Get the first letter of the hand as a string
        String handLetter = hand.substring(0, 1);

        // Offset
        this.addSetting(new OffsetSetting(handLetter + "OffsetX", 0.0d));
        this.addSetting(new OffsetSetting(handLetter + "OffsetY", 0.0d));
        this.addSetting(new OffsetSetting(handLetter + "OffsetZ", 0.0d));

        // Scale
        this.addSetting(new ScaleSetting(handLetter + "ScaleX", 1.0d));
        this.addSetting(new ScaleSetting(handLetter + "ScaleY", 1.0d));
        this.addSetting(new ScaleSetting(handLetter + "ScaleZ", 1.0d));
    }

    public ItemRenderTweaks() {
        super("ItemRenderTweaks");

        this.createHandSettings("Left");
        this.createHandSettings("Right");

        this.setDescription("Allows you to change how held items are rendered.");
        this.setCategory("Render");
    }

    private static class HandRenderSettings {
        private String prefix;
        private Module parent; // prob a better way just to get the container object but whatever.

        private Setting getSetting(String name) {
            return parent.getSetting(String.format("%s%s", this.prefix, name));
        }

        public Vec3d offset() {
            return new Vec3d(
                (double)this.getSetting("OffsetX").value,
                (double)this.getSetting("OffsetY").value,
                (double)this.getSetting("OffsetZ").value
            );
        }

        public Vec3d scale() {
            return new Vec3d(
                (double)this.getSetting("ScaleX").value,
                (double)this.getSetting("ScaleY").value,
                (double)this.getSetting("ScaleZ").value
            );
        }

        HandRenderSettings(String prefix, Module parent) {
            this.prefix = prefix;
            this.parent = parent;
        }
    }

    @Override
    public void activate() {
        this.addListen(RenderItemEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderItemEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderItemEvent": {
                // Get the event
                RenderItemEvent e = (RenderItemEvent)event;

                // We want to see it if there are other players.
                if (e.entity != ComoClient.me()) break;

                // Make sure that they are in first person
                if (!e.renderMode.isFirstPerson()) break;

                HandRenderSettings rs;
                // Handle the different modes
                switch (e.renderMode) {
                    case FIRST_PERSON_LEFT_HAND: {
                        if (!(boolean)this.getSetting("LeftHand").value) return;
                        rs = new HandRenderSettings("L", this);

                        break;
                    }
                    case FIRST_PERSON_RIGHT_HAND: {
                        if (!(boolean)this.getSetting("RightHand").value) return;
                        rs = new HandRenderSettings("R", this);

                        break;
                    }
                    default: return;
                }

                Vec3d scale = rs.scale();
                Vec3d offset = rs.offset();

                e.mStack.scale((float)scale.x, (float)scale.y, (float)scale.z);
                e.mStack.translate(offset.x, offset.y, offset.z);

                break;
            }
        
        }
    }
    
}
