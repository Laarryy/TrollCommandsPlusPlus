package me.egg82.tcpp.events.player.playerDeath;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathBurn implements Consumer<PlayerDeathEvent> {
    public PlayerDeathBurn() { }

    public void accept(PlayerDeathEvent event) {
        Set<UUID> set = CollectionProvider.getSet("burn");
        set.remove(event.getEntity().getUniqueId());
    }
}
