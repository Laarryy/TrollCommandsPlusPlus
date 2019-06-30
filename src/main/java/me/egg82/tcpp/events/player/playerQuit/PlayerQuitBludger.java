package me.egg82.tcpp.events.player.playerQuit;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitBludger implements Consumer<PlayerQuitEvent> {
    public PlayerQuitBludger() { }

    public void accept(PlayerQuitEvent event) {
        Set<UUID> set = CollectionProvider.getSet("bludger");

        if (set.remove(event.getPlayer().getUniqueId())) {
            ConcurrentMap<UUID, UUID> map = CollectionProvider.getMap("bludger");

            UUID fireballUUUID = map.remove(event.getPlayer().getUniqueId());
            if (fireballUUUID != null) {
                Entity fireball = Bukkit.getEntity(fireballUUUID);
                if (fireball instanceof Fireball) {
                    fireball.remove();
                }
            }
        }
    }
}
