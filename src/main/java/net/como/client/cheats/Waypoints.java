package net.como.client.cheats;

import net.como.client.events.OnRenderEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.WaypointSystem;
import net.como.client.structures.WaypointSystem.Waypoint;
import net.como.client.structures.events.Event;
import net.como.client.utils.RenderUtils;

public class Waypoints extends Cheat {
    public WaypointSystem waypoints = new WaypointSystem();

    public Waypoints() {
        super("Waypoints");
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;

                for (String name : this.waypoints.getWaypoints()) {
                    Waypoint waypoint = this.waypoints.getWaypoint(name);
                    if (!waypoint.enabled) continue;

                    RenderUtils.drawTracer(e.mStack, waypoint.pos, e.tickDelta);
                }

                break;
            }
        }
    }
}
