package net.como.client.interfaces;

import java.util.HashMap;

public interface Flatternable {
    public HashMap<String, String> flatten();
    public void lift(HashMap<String, String> flat);
}
