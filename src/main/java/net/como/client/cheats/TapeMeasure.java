package net.como.client.cheats;

import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.como.client.utils.ChatUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

public class TapeMeasure extends Cheat {

    BlockPos start, end;
    Integer clickCount = 0;

    public TapeMeasure() {
        super("TapeMeasure");

        this.settings.addSetting(new Setting("PyDistance", true));

        this.description = "Measure the distance between two points.";
    }

    // Gonna be real with you, I haven't made up my mind on how I am going to display it yet so lemme just do this!
    private void displayMessage(String results) {
        ChatUtils.displayMessage(String.format("%s[%sTapeMeasure%s] %s", ChatUtils.WHITE, ChatUtils.GREEN, ChatUtils.WHITE, results));
    }

    private void handlePyDistance(BlockPos deltaVector) {
        Double x = (double)deltaVector.getX();
        Double y = (double)deltaVector.getY();
        Double z = (double)deltaVector.getZ();

        Double actualDistance = Math.sqrt(x*x + y*y + z*z);

        actualDistance = actualDistance != 0 ? actualDistance + 1 : 0;

        displayMessage(String.format("Your displacement is %f blocks.", actualDistance));
    }

    private void handleVecDistance(BlockPos deltaVector) {
        Integer dX = (int)Math.abs(deltaVector.getX());
        Integer dY = (int)Math.abs(deltaVector.getY());
        Integer dZ = (int)Math.abs(deltaVector.getZ());

        dX = dX != 0 ? dX + 1 : 0;
        dY = dY != 0 ? dY + 1 : 0;
        dZ = dZ != 0 ? dZ + 1 : 0;

        displayMessage(String.format("The absolute of your displacement is X: %d, Y: %d, Z: %d", dX, dY, dZ));
    }

    @Override
    public void activate() {
        this.displayMessage("Hit the two blocks you want to measure the distance between.");
        clickCount = 0;

        super.activate();
    }

    @Override
    public void recieveEvent(String event, Object[] args) {
        switch (event) {
            case "onSendPacket": {
                if (args[0] instanceof net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket) {
                    PlayerActionC2SPacket packet = (PlayerActionC2SPacket)args[0];

                    if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;

                    // Check if this is the first hit or not.
                    if (clickCount % 2 == 0) {
                        // Start Pos
                        start = packet.getPos();

                        clickCount++;
                        return;
                    }
                    
                    // End Pos
                    end = packet.getPos();

                    // Calculate Distance
                    BlockPos delta = end.subtract(start);

                    // Find what the user wants
                    Boolean pythagoreanDistance = (Boolean)this.settings.getSetting("PyDistance").value;
                    
                    // Do what the user wants
                    if (pythagoreanDistance) this.handlePyDistance(delta);
                    else this.handleVecDistance(delta);

                    clickCount++;
                }
            }
        }
    }
}
