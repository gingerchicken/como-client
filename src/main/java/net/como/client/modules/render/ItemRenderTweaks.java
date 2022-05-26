package net.como.client.modules.render;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.render.RenderItemEvent;
import net.como.client.misc.settings.Setting;
import net.como.client.modules.Module;
import net.minecraft.util.math.Vec3d;

public class ItemRenderTweaks extends Module {
    public ItemRenderTweaks() {
        super("ItemRenderTweaks");

        // Right Hand
        this.addSetting(new Setting("RightHand", true));

        // Offset
        this.addSetting(new Setting("ROffsetX", 0.0d));
        this.addSetting(new Setting("ROffsetY", 0.0d));
        this.addSetting(new Setting("ROffsetZ", 0.0d));

        // Scale
        this.addSetting(new Setting("RScaleX", 1.0d));
        this.addSetting(new Setting("RScaleY", 1.0d));
        this.addSetting(new Setting("RScaleZ", 1.0d));

        // Left Hand
        this.addSetting(new Setting("LeftHand", true));
        
        // Offset
        this.addSetting(new Setting("LOffsetX", 0.0d));
        this.addSetting(new Setting("LOffsetY", 0.0d));
        this.addSetting(new Setting("LOffsetZ", 0.0d));

        // Left Scale
        this.addSetting(new Setting("LScaleX", 1.0d));
        this.addSetting(new Setting("LScaleY", 1.0d));
        this.addSetting(new Setting("LScaleZ", 1.0d));

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
