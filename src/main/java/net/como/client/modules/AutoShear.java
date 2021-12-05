package net.como.client.modules;

import java.util.HashMap;

import net.como.client.structures.events.Event;
import net.como.client.ComoClient;
import net.como.client.events.RenderEntityEvent;
import net.como.client.structures.Module;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class AutoShear extends Module {

    public AutoShear() {
        super("AutoShear");

        this.addSetting(new Setting("MaxDistance", 3d));
        this.addSetting(new Setting("DesiredColours", new HashMap<String, Boolean>()));

        this.description = "Shear sheep with a specific colour of wool automatically.";
    }

    @SuppressWarnings("unchecked")
    public void shear(SheepEntity sheep) {
        HashMap<String, String> desiredColours = (HashMap<String, String>)this.getSetting("DesiredColours").value;

        // Get the sheep colour
        String sheepColour = sheep.getColor().getName().toLowerCase();

        // Make sure that we want that wool
        if (!desiredColours.containsKey(sheepColour)) return;

        PlayerInteractEntityC2SPacket shearPacket = PlayerInteractEntityC2SPacket.interact(sheep, ComoClient.me().isSneaking(), Hand.MAIN_HAND);
        ComoClient.me().networkHandler.sendPacket(shearPacket);

        // TODO I swear that there are more packets that are normally sent to the server when this occurs?  Please double check because I don't want to make ACs cry for such a small thing.
    }

    @Override
    public void activate() {
        this.addListen(RenderEntityEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderEntityEvent.class);
    }

    public void fireEvent(Event event) {
        if (!(ComoClient.me().getMainHandStack().getItem() instanceof net.minecraft.item.ShearsItem)) return;

        switch (event.getClass().getSimpleName()) {
            case "RenderEntityEvent": {
                // Get the event.
                RenderEntityEvent e = (RenderEntityEvent)event;

                // Make sure it is a sheep.
                if (!(e.entity instanceof SheepEntity)) break;

                // Get the sheep entity
                SheepEntity sheep = (SheepEntity)e.entity;
                
                // Make sure that they are shearable.
                if (!sheep.isShearable()) break;

                Double maxDistance = (Double)this.getSetting("MaxDistance").value;

                // Make sure that the sheep is in range.
                if (sheep.distanceTo(ComoClient.me()) > maxDistance) break;

                // Shear the sheep
                this.shear(sheep);
            }
        }
    }
}
