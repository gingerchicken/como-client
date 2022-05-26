package net.como.client.modules.combat;

import java.util.Random;

import net.como.client.ComoClient;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.interfaces.mixin.IClient;
import net.como.client.misc.settings.Setting;
import net.como.client.modules.Module;

public class FastAttack extends Module {
    Random random = new Random();

    public FastAttack() {
        super("FastAttack");
        
        this.setDescription("Quickly swings your fist when you attack something.");

        this.addSetting(new Setting("CPS", 10));
        this.addSetting(new Setting("RandomDelay", true));
        this.addSetting(new Setting("MaxDelay", 0.1d));

        this.setCategory("Combat");
    }
 
    private Double getRandomDelay() {
        if (!this.getBoolSetting("RandomDelay")) return 0d;

        return random.nextDouble(0, this.getDoubleSetting("MaxDelay"));
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.nextClickTime = 0d;
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private Double nextClickTime = 0d;

    private void click() {
        Double cur = ComoClient.getCurrentTime();

        // Make sure we should click
        if (this.nextClickTime > cur) return;

        // Call the doAttack function on the client
        IClient client = (IClient)(ComoClient.getClient());
        client.setAttackCooldown(0);
        client.performAttack();

        // Calculate the next time we should hit
        this.nextClickTime = cur + 1d/(double)this.getIntSetting("CPS") + this.getRandomDelay();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (!ComoClient.getClient().options.attackKey.isPressed()) return;
                
                this.click();

                break;
            }
        }
    }
}
