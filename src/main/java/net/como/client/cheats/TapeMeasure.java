package net.como.client.cheats;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.MathsUtil;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

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

    private VertexBuffer blockBox;

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

        blockBox = new VertexBuffer();
		Box bb = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBox(bb, blockBox);
    }

    @Override
	public void deactivate() {
		if (blockBox != null) blockBox.close();
	}

    private void renderReadings(MatrixStack mStack) {
        // GL settings
        GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Render Section
        mStack.push();
        RenderUtils.applyRegionalRenderOffset(mStack);
        
        BlockPos camPos = RenderUtils.getCameraBlockPos();
        int regionX = (camPos.getX() >> 9) * 512;
        int regionZ = (camPos.getZ() >> 9) * 512;

        // // Check the settings
        this.renderLength(regionX, regionZ, mStack);

        // Pop the stack
        mStack.pop();
        
        // GL resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    private void renderBlock(BlockPos bPos, int regionX, int regionZ, MatrixStack mStack) {
        // Load the renderer
        RenderSystem.setShader(GameRenderer::getPositionShader);

        // Push a new item to the render stack
        mStack.push();

        // Translate the point of rendering
        mStack.translate(
            (bPos.getX() + 0.5) - regionX,
            bPos.getY(),
            (bPos.getZ() + 0.5) - regionZ
        );
        
        // Update the size of the box.
        mStack.scale(1, 1, 1);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        
        // Make it so it is our mobBox.
        Shader shader = RenderSystem.getShader();
        Matrix4f matrix4f = RenderSystem.getProjectionMatrix();
        blockBox.setShader(mStack.peek().getModel(), matrix4f, shader);
        
        // Pop the stack (i.e. render it)
        mStack.pop();
    }

    private void renderLength(int regionX, int regionZ, MatrixStack mStack) {
        // Get the difference between the blocks
        BlockPos delta = this.start.add(this.end.multiply(-1));

        BlockPos absDelta = new BlockPos(
            Math.abs(delta.getX()),
            Math.abs(delta.getY()),
            Math.abs(delta.getZ())
        );

        int signX = MathsUtil.getSign(delta.getX());
        int signY = MathsUtil.getSign(delta.getY());
        int signZ = MathsUtil.getSign(delta.getZ());

        // Render all of the different blocks
        BlockPos origin = this.start;
        
        // Firstly render the origin
        this.renderBlock(origin, regionX, regionZ, mStack);

        // Render X
        for (int i = 1; i <= absDelta.getX(); i++) {
            BlockPos target = origin.add(i*-signX, 0, 0);

            this.renderBlock(target, regionX, regionZ, mStack);
        }
        origin = origin.add(-delta.getX(), 0, 0);

        // Render Z
        for (int i = 1; i <= absDelta.getZ(); i++) {
            BlockPos target = origin.add(0, 0, i*-signZ);

            this.renderBlock(target, regionX, regionZ, mStack);
        }
        origin = origin.add(0, 0, -delta.getZ());

        // Render Y
        for (int i = 1; i <= absDelta.getY(); i++) {
            BlockPos target = origin.add(0, i*-signY, 0);

            this.renderBlock(target, regionX, regionZ, mStack);
        }
        origin = origin.add(0, -delta.getY(), 0);
	}

    @Override
    public void recieveEvent(String event, Object[] args) {
        switch (event) {
            case "onRenderWorld": {
                if (clickCount < 2 || clickCount % 2 != 0) break;

                MatrixStack mStack = (MatrixStack) args[0];

                renderReadings(mStack);
                break;
            }
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

                    return;
                }
            }
        }
    }
}
