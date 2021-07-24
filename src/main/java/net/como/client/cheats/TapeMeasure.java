package net.como.client.cheats;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.structures.Cheat;
import net.como.client.structures.Setting;
import net.como.client.utils.ChatUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;
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

    // TODO maybe move this to some form of Util?
    private Vec3d blockPosToVec3d(BlockPos bP) {
        return new Vec3d(bP.getX() + 0.5, bP.getY() + 0.5, bP.getZ() + 0.5);
    }

    private void renderBlock(BlockPos bPos, int regionX, int regionZ, MatrixStack mStack) {
        
    }

    private void renderLength(int regionX, int regionZ, MatrixStack mStack) {
        Vec3d start = this.blockPosToVec3d(this.start);
        Vec3d end = this.blockPosToVec3d(this.end);
        
        // Get our extraSize setting
		Float extraSize = (float)(double) this.settings.getSetting("BoxPadding").value;

        // Load the renderer
        RenderSystem.setShader(GameRenderer::getPositionShader);

        // Push a new item to the render stack
        mStack.push();

        // Translate the point of rendering
        mStack.translate(
            start.x - regionX,
            start.y,
            start.z - regionZ
        );
        
        // Update the size of the box.
        mStack.scale(32, 32, 32);
        
        // Make it so it is our mobBox.
        Shader shader = RenderSystem.getShader();
        Matrix4f matrix4f = RenderSystem.getProjectionMatrix();
        blockBox.setShader(mStack.peek().getModel(), matrix4f, shader);
        
        // Pop the stack (i.e. render it)
        mStack.pop();
	}

    @Override
    public void recieveEvent(String event, Object[] args) {
        switch (event) {
            // TODO add a hook that renders only when one frame is rendered as this is horrible :(
            case "onRenderEntity": {
                if (clickCount < 2 || clickCount % 2 != 0) break;

                MatrixStack mStack = (MatrixStack) args[5];

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
