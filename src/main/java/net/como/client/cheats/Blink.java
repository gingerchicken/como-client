package net.como.client.cheats;

import net.como.client.structures.Cheat;

public class Blink extends Cheat {

    public Blink() {
        super("BlinkMode");

        this.description = "Delay your packets being sent.";
    }

    // TODO this is not how blink works lol - store the packets and send them.
    // Actually make this work at some point
}