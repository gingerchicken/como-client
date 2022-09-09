package net.como.client.utils;

import net.fabricmc.loader.api.FabricLoader;

public class ThirdPartyUtils {
    /**
     * Checks if meteor client is loaded
     * @return if meteor client is loaded
     */
    public static boolean isMeteorLoaded() {
        return FabricLoader.getInstance().isModLoaded("meteor-client");
    }

    /**
     * Checks if coffee client is loaded
     * @return if coffee client is loaded
     */
    public static boolean isCoffeeLoaded() {
        return FabricLoader.getInstance().isModLoaded("coffee");
    }

    public static boolean isOtherClientLoaded() {
        return isMeteorLoaded() || isCoffeeLoaded();
    }
}
