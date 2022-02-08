package net.como.client;

import net.como.client.structures.Colour;

public class GeneralConfig {
    public Colour storageColour = new Colour(255, 223, 0, 255);
    public Colour entityColour  = new Colour(255, 255, 255, 255);
    public String font = "como-client:como";
    public String commandPrefix = ".";
    public String alterativeCommandPrefix = ",";
    public Integer menuKey = 344;

    public GeneralConfig() {

    }
}
