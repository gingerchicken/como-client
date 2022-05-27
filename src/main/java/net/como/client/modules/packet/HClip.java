package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.modules.Module;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.MathsUtils;
import net.minecraft.util.math.Vec3d;

public class HClip extends Module {

    public HClip() {
        super("HClip");
        this.setDescription("Teleports the player a set amount of blocks away");

        this.setCategory("Packet");

        this.addSetting(new Setting("X", 0d));
        this.addSetting(new Setting("Y", 0d));
        this.addSetting(new Setting("Z", 0d));

        this.addSetting(new Setting("ChatMessage", true));

        this.addSetting(new Setting("AngleRelative", false));
    }

    @Override
    public void onEnabled() {
        ChatUtils.hideNextChat = true;
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        ChatUtils.hideNextChat = true;
        super.onDisabled();
    }

    private void showWoosh() {
        int realDistance = (int)Math.round(this.getOffset().distanceTo(Vec3d.ZERO));

        // Clamp it
        int distance = realDistance;

        distance = distance < 2 ? 2 : distance;
        distance = distance > 8 ? 8 : distance;

        String oo = "";
        for (int i = 0; i < distance; i++) {
            oo = oo.concat("o");
        }

        oo = ChatUtils.randomCase(oo);

        this.displayMessage(
            String.format("W%ssh! You moved %d blocks away.", oo, realDistance)
        );
    }

    public Vec3d getOffset() {
        return new Vec3d(
            this.getDoubleSetting("X"),
            this.getDoubleSetting("Y"),
            this.getDoubleSetting("Z")
        );
    }

    public Vec3d nextPos() {
        Vec3d offset = this.getOffset();
        
        if (this.getBoolSetting("AngleRelative")) {
            // Calculate the sides
            Vec3d forward = MathsUtils.getForwardVelocity(ComoClient.me());
            Vec3d right   = MathsUtils.getRightVelocity(ComoClient.me());

            // Get the results
            Vec3d result = Vec3d.ZERO;
            result = result.add(forward.multiply(offset.getX()));
            result = result.add(right.multiply(offset.getZ()));
            
            // We don't have a vertical component
            result = result.add(0, offset.getY(), 0);

            // Set the result
            offset = result;
        }

        return ComoClient.me().getPos().add(offset);
    }

    @Override
    public void activate() {
        Vec3d pos = this.nextPos();
        ComoClient.me().setPos(pos.getX(), pos.getY(), pos.getZ());

        if (this.getBoolSetting("ChatMessage")) this.showWoosh();

        this.disable();
    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void fireEvent(Event event) {
        // TODO Auto-generated method stub
        
    }
}
