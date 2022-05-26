package net.como.client.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.util.math.Vec3d;

public class WaypointSystem {
    public static class Waypoint {
        public Vec3d pos;
        public int colour = 0x0;
        public boolean enabled = true;

        public Waypoint(Vec3d pos) {
            this.pos = pos;
        }
        
        public Vec3d translateNether() {
            return new Vec3d((int)pos.x/8, pos.y, (int)pos.z/8);
        }

        public boolean toggle() {
            enabled = !enabled;

            return enabled;
        }

        public void enable() {
            enabled = true;
        }

        public void disable() {
            enabled = false;
        }
    }

    private HashMap<String, Waypoint> waypoints = new HashMap<String, Waypoint>();

    public WaypointSystem() {

    }

    public List<String> getWaypoints() {
        List<String> waypointNames = new ArrayList<String>();

        for (String name : this.waypoints.keySet()) {
            waypointNames.add(name);
        }

        return waypointNames;
    }

    public Waypoint getWaypoint(String name) {
        if (!this.hasWaypoint(name)) return null;

        return this.waypoints.get(name);
    }

    public boolean hasWaypoint(String name) {
        return this.waypoints.containsKey(name);
    }

    public boolean addWaypoint(String name, Waypoint waypoint) {
        if (this.hasWaypoint(name)) return false;

        this.waypoints.put(name, waypoint);
        return true;
    }

    public boolean removeWaypoint(String name) {
        if (!this.hasWaypoint(name)) return false;
        
        this.waypoints.remove(name);
        return true;
    }
}
