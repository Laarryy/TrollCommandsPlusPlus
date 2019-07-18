package me.egg82.tcpp.events.player.playerMove;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.services.entity.EntityDamageHandler;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveBrittle implements Consumer<PlayerMoveEvent> {
    public PlayerMoveBrittle() { }

    public void accept(PlayerMoveEvent event) {
        Set<UUID> set = CollectionProvider.getSet("brittle");

        if (!set.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        Location to = event.getTo();

        if (to.getY() == to.getBlockY() && to.getY() < event.getFrom().getY()) {
            event.getPlayer().setHealth(0.0d);
            EntityDamageHandler.damage(event.getPlayer(), Double.MAX_VALUE, EntityDamageEvent.DamageCause.FALL);
        }
    }
}
