package me.egg82.tcpp.events.entity.entityExplode;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeBludger implements Consumer<EntityExplodeEvent> {
    public EntityExplodeBludger() { }

    public void accept(EntityExplodeEvent event) {
        Set<UUID> set = CollectionProvider.getSet("bludger");
        ConcurrentMap<UUID, UUID> map = CollectionProvider.getMap("bludger");

        UUID playerUUUID = null;
        for (Map.Entry<UUID, UUID> kvp : map.entrySet()) {
            if (kvp.getValue().equals(event.getEntity().getUniqueId())) {
                playerUUUID = kvp.getKey();
                map.remove(kvp.getKey());
                break;
            }
        }

        if (playerUUUID != null) {
            set.remove(playerUUUID);
        }
    }
}
