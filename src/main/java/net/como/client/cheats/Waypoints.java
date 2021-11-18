package net.como.client.cheats;

import net.como.client.events.OnRenderEvent;
import net.como.client.events.RenderWorldViewBobbingEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.WaypointSystem;
import net.como.client.structures.WaypointSystem.Waypoint;
import net.como.client.structures.events.Event;
import net.como.client.utils.RenderUtils;

public class Waypoints extends Cheat {
    public WaypointSystem waypoints = new WaypointSystem();

    public Waypoints() {
        super("Waypoints");

        this.description = "Renders where waypoints are in the world.";
    }

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
        this.addListen(RenderWorldViewBobbingEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        this.removeListen(RenderWorldViewBobbingEvent.class);
    }

    private boolean isActive = false;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldViewBobbingEvent": {
                if (!this.isActive) return;

                // Stabilise the tracers
                RenderWorldViewBobbingEvent e = (RenderWorldViewBobbingEvent)event;
                e.cancel = true;

                break;
            }
            case "OnRenderEvent": {
                OnRenderEvent e = (OnRenderEvent)event;

                this.isActive = false;
                for (String name : this.waypoints.getWaypoints()) {
                    Waypoint waypoint = this.waypoints.getWaypoint(name);
                    if (!waypoint.enabled) continue;

                    RenderUtils.drawTracer(e.mStack, waypoint.pos, e.tickDelta);
                    this.isActive = true;
                }

                break;
            }
        }
    }
}
