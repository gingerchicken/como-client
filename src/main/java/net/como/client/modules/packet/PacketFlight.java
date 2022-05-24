package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.ClientUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PacketFlight extends Module {

    public PacketFlight() {
        super("PacketFlight");

        this.setCategory("Packet");

        this.setDescription("Sets your position relative to your controls.");

        this.addSetting(new Setting("Step", 0.25d));
        this.addSetting(new Setting("ZeroVelocity", true));
        this.addSetting(new Setting("Elytra", false));
        this.addSetting(new Setting("Noclip", true));
    }
    
    @Override
    public String listOption() {
        return this.getBoolSetting("Elytra") ? "Elytra+" : "WorldFly";
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private Vec3d getOffset() {
        return ClientUtils.getControlVelocity(ComoClient.me(), this.getDoubleSetting("Step"), true);
    }

    private Vec3d nextPos() {
        return ComoClient.me().getPos().add(this.getOffset());
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                // Elytra only mode!
                if (this.getBoolSetting("Elytra")) {
                    // Make sure that we have an elytra equip
                    if (!ClientUtils.hasElytraEquipt()) break;

                    // Make sure that we are using the elytra
                    if (!ComoClient.me().isFallFlying()) break;
                }

                if (this.getBoolSetting("ZeroVelocity")) ComoClient.me().setVelocity(Vec3d.ZERO);

                Vec3d pos = this.nextPos();
                if (!this.getBoolSetting("Noclip")) {
                    BlockPos blockPos = new BlockPos(pos);

                    BlockState state = BlockUtils.getState(blockPos);
                    if (!state.isAir()) break;
                }

                ComoClient.me().setPos(pos.getX(), pos.getY(), pos.getZ());

                break;
            }
        }
    }
}
