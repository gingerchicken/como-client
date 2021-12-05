package net.como.client.modules;

import net.como.client.CheatClient;
import net.como.client.structures.Cheat;
import net.minecraft.client.MinecraftClient;

public class FullBright extends Cheat {
    private Double normalGamma = 0d;

    public FullBright() {
        super("FullBright");
        
        this.description = "Allows you to see anywhere as if it was day.";
    }
    
    @Override
    public void activate() {
        MinecraftClient client = CheatClient.getClient();

        this.normalGamma = client.options.gamma;
        client.options.gamma = 16;
    }

    @Override
    public void deactivate() {
        MinecraftClient client = CheatClient.getClient();

        client.options.gamma = this.normalGamma;
    }
}
