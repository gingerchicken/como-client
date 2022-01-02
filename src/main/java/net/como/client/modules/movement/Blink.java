package net.como.client.modules.movement;

import net.como.client.structures.Module;

public class Blink extends Module {

    public Blink() {
        super("BlinkMode");

        this.description = "Delay your packets being sent.";

        this.setCategory("Movement");
    }
  
    // TODO Actually make this work at some point
}