package net.como.client.modules.dupes;

import net.como.client.structures.Module;
import net.como.client.structures.settings.Setting;

public class CraftingDupe extends Module {

    public CraftingDupe() {
        super("CraftingDupe");
        this.description = "An old drop dupe for versions 1.17 and earlier";

        this.setCategory("Dupe");
        this.addSetting(new Setting("ShowTip", true));
    }
    
    @Override
    public void activate() {
        if (this.getBoolSetting("ShowTip")) this.displayMessage("Remember, this will only work on servers that are 1.17 and lower, you can connect to them via multiconnect.");
    }
}
