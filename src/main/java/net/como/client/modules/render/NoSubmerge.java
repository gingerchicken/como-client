package net.como.client.modules.render;

import net.como.client.events.OnSubmersionTypeEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.client.render.CameraSubmersionType;

public class NoSubmerge extends Module {
    public NoSubmerge() {
        super("NoSubmerge");

        this.addSetting(new Setting("Lava", true));
        this.addSetting(new Setting("Water", true));
        this.addSetting(new Setting("PowderSnow", true));
    
        this.description = "Allows submerge overlays to be toggled.";
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(OnSubmersionTypeEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnSubmersionTypeEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnSubmersionTypeEvent": {
                OnSubmersionTypeEvent e = (OnSubmersionTypeEvent)event;

                Setting setting = null;
                switch (e.cir.getReturnValue()) {
                    case LAVA: {
                        setting = this.getSetting("Lava");
                        break;
                    }
                    case POWDER_SNOW: {
                        setting = this.getSetting("PowderSnow");
                        break;
                    }
                    case WATER: {
                        setting = this.getSetting("Water");
                        break;
                    }
                    default: {
                        break;
                    }
                }

                // Make sure it is something we catch.
                if (setting == null) break;

                // Hide it if we need to.
                if ((boolean)setting.value) e.cir.setReturnValue(CameraSubmersionType.NONE);

                break;
            }
        }
    }
}
