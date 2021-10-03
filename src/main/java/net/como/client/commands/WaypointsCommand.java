package net.como.client.commands;

import java.util.List;

import net.como.client.CheatClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.structures.WaypointSystem;
import net.como.client.structures.WaypointSystem.Waypoint;
import net.como.client.utils.ChatUtils;
import net.minecraft.util.math.Vec3d;

public class WaypointsCommand extends CommandNode {
    public static class SubCommand extends Command {
        public WaypointSystem system;
    
        public SubCommand(String command, String helpText, String description, WaypointSystem system) {
            super(command, helpText, description);
        
            this.commandDisplay = "Waypoints";
            this.system = system;
        }

        public Vec3d vec3dFromString(String x, String y, String z) {
            return new Vec3d(
                Double.parseDouble(x),
                Double.parseDouble(y),
                Double.parseDouble(z)
            );
        }
    }
    public static class Show extends SubCommand {
        public Show(WaypointSystem system) {
            super("list", "", "Lists all the established waypoints", system);
        }

        private void displayWaypoint(String name) {
            Waypoint waypoint = system.getWaypoint(name);        
            Vec3d pos = waypoint.pos;

            String enabledMessage = waypoint.enabled ? String.format("%sENABLED", ChatUtils.GREEN) : String.format("%sDISABLED", ChatUtils.RED);
            this.displayChatMessage(
                String.format("-> [%s%s] %s - %f %f %f", enabledMessage, ChatUtils.WHITE, name, pos.x, pos.y, pos.z)
            );
        }

        @Override
        public Boolean trigger(String[] args) {
            List<String> waypoints = this.system.getWaypoints();

            // If there are no waypoints then tell them so.
            if (waypoints.size() == 0) {
                this.displayChatMessage("There are currently no waypoints to display.");

                return true;
            }

            // Display all of the waypoints
            this.displayChatMessage("List of waypoints:");
            for (String name : waypoints) {
                this.displayWaypoint(name);
            }

            return true;
        }
    }
    public static class Add extends SubCommand {
        public Add(WaypointSystem system) {
            super("add", "<name> <current|x> <optional y|z> <z>", "Adds a new waypoint", system);
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            return !(args.length == 2 && args[1].equals("current")) && (args.length < 3);
        }

        @Override
        public Boolean trigger(String[] args) {
            if (this.handleHelp(args)) return true;

            // TODO what to do if they are in the nether?

            String name     = args[0];
            boolean current = args[1].equals("current");

            Vec3d pos;
            if (current) {
                pos = CheatClient.me().getPos();
            } else {
                pos = args.length == 3 
                    ? this.vec3dFromString(args[1], "0", args[2]).add(0, CheatClient.me().getY(), 0)
                    : this.vec3dFromString(args[1], args[2], args[3]);
            }
            
            // Handle the nether HERE
            // TODO ADD COLOURS

            boolean added = system.addWaypoint(name, new Waypoint(pos));
            if (!added) {
                this.displayChatMessage(String.format("Unable to add waypoint '%s,' as it already exists.", name));
                return true;
            }

            return true;
        }
    }
    public static class Remove extends SubCommand {
        public Remove(WaypointSystem system) {
            super("remove", "<name>", "Removes a waypoint", system);
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            return args.length == 0;
        }

        @Override
        public Boolean trigger(String[] args) {
            if (this.handleHelp(args)) return true;

            String name = args[0];
            
            boolean removed = this.system.removeWaypoint(name);
            if (!removed) {
                this.displayChatMessage(
                    String.format("Unable to remove waypoint with name '%s.'", name)
                );
            }
            return true;
        }
    }
    public static class Toggle extends SubCommand {
        public Toggle(WaypointSystem system) {
            super("toggle", "<name>", "Toggles the display of a waypoint", system);
        }

        @Override
        public boolean shouldShowHelp(String[] args) {
            return args.length == 0;
        }

        @Override
        public Boolean trigger(String[] args) {
            if (handleHelp(args)) return true;

            String name = args[0];
            if (!system.hasWaypoint(name)) {
                this.displayChatMessage(String.format("Unable to find waypoint '%s.'", name));
                return true;
            }

            Waypoint waypoint = system.getWaypoint(name);
            waypoint.toggle();

            String enabledMessage = waypoint.enabled ? String.format("%senabled", ChatUtils.GREEN) : String.format("%sdisabled", ChatUtils.RED);
            enabledMessage = String.format("%s%s", enabledMessage, ChatUtils.WHITE);

            this.displayChatMessage(String.format("%s has been %s.", name, enabledMessage));

            return true;
        }
    }

    public WaypointsCommand(WaypointSystem system) {
        super("waypoint", "Manage your waypoints");

        this.addSubCommand(new Show(system));
        this.addSubCommand(new Add(system));
        this.addSubCommand(new Remove(system));
        this.addSubCommand(new Toggle(system));
    }
    
}
