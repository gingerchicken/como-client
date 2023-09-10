package net.como.client.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.como.client.ComoClient;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.client.DisconnectEvent;
import net.como.client.events.packet.OnEntityStatusEvent;
import net.como.client.events.screen.DeathEvent;
import net.como.client.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class TotemPopCount extends Module {
    public TotemPopCount() {
        super("TotemPopCount");

        this.setDescription("This counts the total number of totems used by a player before death.");

        // Default One min
        this.addSetting(new Setting("CountDuration", 60d) {{
            this.setDescription("The duration in seconds to count the totems before resetting.");

            this.setMax(3600d);
            this.setMin(1d);
        }});
        this.addSetting(new Setting("DeathMessage", true) {{
            this.setDescription("Show how many totems someone has used before death.");
        }});
        
        this.setCategory("Combat");
    }

    private Integer localPopCount = 0;

    public Integer getTotalPops() {
        return this.localPopCount / 2;
    }

    private static class PlayerEntry {
        private Integer popPackets = 0;
        public Double lastValid;

        public PlayerEntry() {
            this.valid();
        }

        public Integer onPopPacket() {
            this.valid();
            return ++this.popPackets;
        }

        public Boolean isPop() {
            return this.popPackets % 2 == 0;
        }

        public Integer getPops() {
            return this.popPackets / 2;
        }

        public Double getLastValid() {
            return this.lastValid;
        }

        public void valid() {
            this.lastValid = ComoClient.getCurrentTime();
        }
    }

    private HashMap<UUID, PlayerEntry> entries = new HashMap<>();

    @Override
    public String listOption() {
        return (this.getTotalPops()).toString();
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(DeathEvent.class);
        this.addListen(OnEntityStatusEvent.class);
        this.addListen(DisconnectEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(DisconnectEvent.class);
        this.removeListen(DeathEvent.class);
        this.removeListen(ClientTickEvent.class);
        this.removeListen(OnEntityStatusEvent.class);
    }

    public PlayerEntry playerPop(PlayerEntity player) {
        if (player == ComoClient.me()) {
            this.localPopCount++;

            return null;
        }

        UUID uuid = player.getUuid();

        if (!this.entries.containsKey(uuid)) {
            this.entries.put(uuid, new PlayerEntry());
        }
        PlayerEntry entry = this.entries.get(uuid);

        entry.onPopPacket();
        return entry;
    }

    private void resetLocal() {
        this.localPopCount = 0;
    }

    private void resetEntries() {
        this.entries.clear();
    }

    private void resetAll() {
        this.resetLocal();
        this.resetEntries();
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "DeathEvent": {
                if (this.getBoolSetting("DeathMessage") && this.getTotalPops() > 0) this.displayMessage(String.format("You used %d totems before you died.", this.getTotalPops()));

                this.resetLocal();
                break;
            }

            case "DisconnectEvent": {
                this.resetAll();
                break;
            }

            case "ClientTickEvent": {
                for (PlayerEntity player : ComoClient.getClient().world.getPlayers()) {
                    UUID uuid = player.getUuid();

                    if (!this.entries.containsKey(uuid)) continue;
                    PlayerEntry entry = this.entries.get(uuid);

                    if (!player.isAlive() && entry.isPop()) {
                        if (this.getBoolSetting("DeathMessage")) this.displayMessage(String.format("%s died after popping a total of %d totems.", player.getEntityName(), entry.getPops()));
                        this.entries.remove(uuid);
                        continue;
                    }

                    this.entries.get(player.getUuid()).valid();
                }

                Double duration = this.getDoubleSetting("CountDuration");

                List<UUID> expiredUUIDs = new ArrayList<>();
                for (UUID uuid : this.entries.keySet()) {
                    PlayerEntry entry = this.entries.get(uuid);

                    Double lastValid = entry.getLastValid();

                    if (ComoClient.getCurrentTime() - lastValid >= duration) {
                        expiredUUIDs.add(uuid);
                    }
                }

                // Remove them separately
                for (UUID uuid : expiredUUIDs) {
                    this.entries.remove(uuid);
                }

                break;
            }

            case "OnEntityStatusEvent": {
                OnEntityStatusEvent e = (OnEntityStatusEvent)event;

                if (e.packet.getStatus() != 35) break;

                Entity entity = e.packet.getEntity(ComoClient.me().getWorld());
                
                // I mean I have no idea how that would work but like whatever.
                if (!(entity instanceof PlayerEntity)) break;
                PlayerEntity player = (PlayerEntity)entity;

                PlayerEntry entry = this.playerPop(player);

                // MC is a bit wacky when handling totem popping.
                if (entry == null || !entry.isPop()) break;

                this.displayMessage(String.format("%s has popped %d totems so far.", player.getEntityName(), entry.getPops()));

                break;
            }
        }
    }
}
