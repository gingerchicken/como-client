package net.como.client.modules.movement;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickMovementEvent;
import net.como.client.events.client.GetVelocityMultiplierEvent;
import net.como.client.modules.Module;
import net.minecraft.client.input.Input;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow");

        this.setDescription("Allows you to never slow down.");

        this.setCategory("Movement");

        this.addSetting(
            new Setting("UseSpeedMultiplier", 1d)
            {{
                setDescription("The speed multiplier for when you are using an item.");
                setMax(5d);
                setMin(0d);
            }}
        );
    }
    
    @Override
    public void activate() {
        this.addListen(GetVelocityMultiplierEvent.class);
        this.addListen(ClientTickMovementEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetVelocityMultiplierEvent.class);
        this.removeListen(ClientTickMovementEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetVelocityMultiplierEvent": {
                GetVelocityMultiplierEvent e = (GetVelocityMultiplierEvent)event;

                e.cir.setReturnValue(1f);
                
                break;
            }
            case "ClientTickMovementEvent": {
                ClientTickMovementEvent e = (ClientTickMovementEvent)event;

                Input input = ComoClient.me().input;

                if (ComoClient.me().isUsingItem()) {
                    float side = input.movementSideways;
                    float forward = input.movementForward;

                    side = side == 0 ? 0 : (side > 0 ? 1 : -1);
                    forward = forward == 0 ? 0 : (forward > 0 ? 1 : -1);
                    
                    // Create the movement vector
                    Vec2f mov = new Vec2f(side, forward);

                    mov = mov.multiply(ComoClient.me().getMovementSpeed() * (float)(double)this.getDoubleSetting("UseSpeedMultiplier"));

                    // Get a velocity from this movement.
                    Vec3d vel = new Vec3d(mov.x, 0, mov.y);

                    // Make this velocity relative to the player's angle.
                    vel = vel.rotateY((float)Math.toRadians(-ComoClient.me().getYaw()));

                    // Add the current vertical component of velocity.
                    vel = vel.add(0, ComoClient.me().getVelocity().y, 0);

                    // Set the player's velocity to this velocity.
                    ComoClient.me().setVelocity(vel);
                }
                
                break;
            }
        }
    }
}
