package me.egg82.tcpp.events.player.playerDeath;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathBrittle implements Consumer<PlayerDeathEvent> {
    public PlayerDeathBrittle() { }

    public void accept(PlayerDeathEvent event) {
        Set<UUID> set = CollectionProvider.getSet("brittle");
        set.remove(event.getEntity().getUniqueId());
    }
}
