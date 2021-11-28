package net.como.client.cheats;

import java.util.HashMap;
import java.util.UUID;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.DeathEvent;
import net.como.client.events.DisconnectEvent;
import net.como.client.events.OnEntityStatusEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class TotemPopCount extends Cheat {
    public TotemPopCount() {
        super("TotemPopCount");

        this.description = "This counts the total number of totems used by a player before death.";

        // Default One min
        this.addSetting(new Setting("CountDuration", 60d));
        this.addSetting(new Setting("DeathMessage", true));
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
            this.lastValid = CheatClient.getCurrentTime();
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
        if (player == CheatClient.me()) {
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
                this.resetLocal();
                break;
            }

            case "DisconnectEvent": {
                this.resetAll();
                break;
            }

            case "ClientTickEvent": {
                for (PlayerEntity player : CheatClient.getClient().world.getPlayers()) {
                    UUID uuid = player.getUuid();

                    if (!this.entries.containsKey(uuid)) continue;
                    PlayerEntry entry = this.entries.get(uuid);

                    if (!player.isAlive() && entry.isPop()) {
                        if (this.getBoolSetting("DeathMessage")) this.displayMessage(String.format("%s died after popping a total of %d totems.", player.getDisplayName().asString(), entry.getPops()));
                        this.entries.remove(uuid);
                        continue;
                    }

                    this.entries.get(player.getUuid()).valid();
                }

                Double duration = this.getDoubleSetting("CountDuration");
                for (UUID uuid : this.entries.keySet()) {
                    PlayerEntry entry = this.entries.get(uuid);

                    Double lastValid = entry.getLastValid();

                    if (CheatClient.getCurrentTime() - lastValid >= duration) {
                        this.entries.remove(uuid);
                    }
                }

                break;
            }

            case "OnEntityStatusEvent": {
                OnEntityStatusEvent e = (OnEntityStatusEvent)event;

                if (e.packet.getStatus() != 35) break;

                Entity entity = e.packet.getEntity(CheatClient.me().world);
                
                // I mean I have no idea how that would work but like whatever.
                if (!(entity instanceof PlayerEntity)) break;
                PlayerEntity player = (PlayerEntity)entity;

                PlayerEntry entry = this.playerPop(player);

                // MC is a bit wacky when handling totem popping.
                if (entry == null || !entry.isPop()) break;

                this.displayMessage(String.format("%s has popped %d totems so far.", player.getDisplayName().asString(), entry.getPops()));

                break;
            }
        }
    }
}
