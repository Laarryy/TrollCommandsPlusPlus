package me.egg82.tcpp.events.entity.entityChangeBlock;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlockAnvil implements Consumer<EntityChangeBlockEvent> {
    public EntityChangeBlockAnvil() { }

    public void accept(EntityChangeBlockEvent event) {
        Set<UUID> set = CollectionProvider.getSet("anvil");

        if (set.remove(event.getEntity().getUniqueId())) {
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }
}
