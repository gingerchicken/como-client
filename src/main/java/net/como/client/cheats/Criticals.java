package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.OnAttackEntityEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Cheat {
    public Criticals() {
        super("Criticals");
    }

    @Override
    public void activate() {
        this.addListen(OnAttackEntityEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnAttackEntityEvent.class);
    }

    private void packetJump() {
        Vec3d v = CheatClient.me().getPos();

        ClientUtils.sendPos(v.add(0, 0.0625d, 0), true);
        ClientUtils.sendPos(v, true);
        ClientUtils.sendPos(v.add(0, 1.1E-5D, 0), false);
        ClientUtils.sendPos(v, false);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnAttackEntityEvent": {
                // Packet mode
                this.packetJump();
                break;
            }
        }
    }
}
