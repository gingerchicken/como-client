package net.como.client.cheats;

import java.util.HashMap;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class AutoShear extends Cheat {

    public AutoShear() {
        super("AutoShear");

        this.addSetting(new Setting("MaxDistance", 3d));
        this.addSetting(new Setting("DesiredColours", new HashMap<String, Boolean>()));

        this.description = "Shear sheep with a specific colour of wool automatically.";
    }

    public void shear(SheepEntity sheep) {
        HashMap<String, String> desiredColours = (HashMap<String, String>)this.getSetting("DesiredColours").value;

        // Get the sheep colour
        String sheepColour = sheep.getColor().getName().toLowerCase();

        // Make sure that we want that wool
        if (!desiredColours.containsKey(sheepColour)) return;

        PlayerInteractEntityC2SPacket shearPacket = PlayerInteractEntityC2SPacket.interact(sheep, CheatClient.me().isSneaking(), Hand.MAIN_HAND); // = new PlayerInteractEntityC2SPacket(sheep, Hand.MAIN_HAND, CheatClient.me().isSneaking());
        CheatClient.me().networkHandler.sendPacket(shearPacket);

        // TODO I swear that there are more packets that are normally sent to the server when this occurs?  Please double check because I don't want to make ACs cry for such a small thing.
    }

    public void recieveEvent(String eventName, Object[] args) {
        switch (eventName) {
            case "onRenderEntity": {
                // Make sure that we are holding shears
                if (!(CheatClient.me().getMainHandStack().getItem() instanceof net.minecraft.item.ShearsItem)) break;

                Entity entity = (Entity)args[0];
                
                // Make sure it is a sheep.
                if (!(entity instanceof SheepEntity)) break;

                SheepEntity sheep = (SheepEntity)entity;
                
                // Make sure that they are shearable.
                if (!sheep.isShearable()) break;

                Double maxDistance = (Double)this.getSetting("MaxDistance").value;

                // Make sure that the sheep is in range.
                if (sheep.distanceTo(CheatClient.me()) > maxDistance) break;

                // Shear the sheep
                this.shear(sheep);
            }
        }
    }
}
