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

        this.addSetting(new Setting("X", 0d) {{
            this.setCategory("Offset");
        }});
        this.addSetting(new Setting("Y", 0d) {{
            this.setCategory("Offset");
        }});
        this.addSetting(new Setting("Z", 0d) {{
            this.setCategory("Offset");
        }});

        this.addSetting(new Setting("ChatMessage", true));

        this.addSetting(new Setting("AngleRelative", false));

        this.addSetting(new Setting("Steps", true));
        this.addSetting(new Setting("StepsAmount", 10) {
            @Override
            public boolean shouldShow() {
                return super.shouldShow() && getBoolSetting("Steps");
            }

            {
                this.setMin(1);
                this.setMax(128);
                this.setDescription("The amount of steps to take");
                this.setCategory("Steps");
            }
        });
        this.addSetting(new Setting("StepDelay", 0d) {
            @Override
            public boolean shouldShow() {
                return super.shouldShow() && getBoolSetting("Steps");
            }

            {
                this.setMin(0d);
                this.setMax(1d);
                this.setDescription("The delay between each step");
                this.setCategory("Steps");
            }
        });
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
        Vec3d travelled = new Vec3d(getDoubleSetting("X"), getDoubleSetting("Y"), getDoubleSetting("Z"));

        int realDistance = (int)Math.round(travelled.distanceTo(Vec3d.ZERO));

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
            this.getComponentOrStep("X"),
            this.getComponentOrStep("Y"),
            this.getComponentOrStep("Z")
        );
    }

    protected double getComponentOrStep(String component) {
        double k = this.getDoubleSetting(component);

        if (!this.getBoolSetting("Steps")) return k;

        return k / (double)this.getIntSetting("StepsAmount");
    }

    public Vec3d nextPos() {
        boolean shouldStep = this.getBoolSetting("Steps");

        Vec3d finalOffset = Vec3d.ZERO;

        for (int i = 0; (!shouldStep && i == 0) || i < this.getIntSetting("StepsAmount"); i++) {
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

            // Add the step to the final offset
            finalOffset = finalOffset.add(offset);
        }

        return ComoClient.me().getPos().add(finalOffset);
    }

    private void performMove() {
        Thread t = new Thread(() -> {
            boolean shouldStep = this.getBoolSetting("Steps");
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
            }
    
            for (int i = 0; ((!shouldStep && i == 0) || (shouldStep && i < this.getIntSetting("StepsAmount"))) && ComoClient.getClient().world != null; i++) {
                Vec3d nextPos = ComoClient.me().getPos().add(offset);
    
                // Update the position
                ComoClient.me().setPos(
                    nextPos.getX(), nextPos.getY(), nextPos.getZ()
                );

                // Wait for the delay
                if (shouldStep) {
                    try {
                        Thread.sleep((long)(this.getDoubleSetting("StepDelay") * 1000));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();
    }

    @Override
    public void activate() {
        this.performMove();

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
