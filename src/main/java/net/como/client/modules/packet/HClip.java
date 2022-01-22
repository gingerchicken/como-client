package net.como.client.modules.packet;

import net.como.client.ComoClient;
import net.como.client.structures.Module;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ChatUtils;
import net.minecraft.util.math.Vec3d;

public class HClip extends Module {

    public HClip() {
        super("HClip");
        this.description = "Teleports the player a set amount of blocks away";

        this.setCategory("Packet");

        this.addSetting(new Setting("X", 0d));
        this.addSetting(new Setting("Y", 0d));
        this.addSetting(new Setting("Z", 0d));

        this.addSetting(new Setting("ChatMessage", true));
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
        return ComoClient.me().getPos().add(this.getOffset());
    }

    @Override
    public void activate() {
        Vec3d pos = this.nextPos();
        ComoClient.me().setPos(pos.getX(), pos.getY(), pos.getZ());

        if (this.getBoolSetting("ChatMessage")) this.showWoosh();

        this.disable();
    }
}
