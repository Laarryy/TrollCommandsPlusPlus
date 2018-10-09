package me.egg82.tcpp.events.entity.entityChangeBlock;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import me.egg82.tcpp.lists.AnvilSet;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class AnvilEventCommand extends EventHandler<EntityChangeBlockEvent> {
    //vars
    private IConcurrentSet<UUID> anvilSet = ServiceLocator.getService(AnvilSet.class);

    //constructor
    public AnvilEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Entity entity = event.getEntity();
        UUID uuid = entity.getUniqueId();

        if (anvilSet.remove(uuid)) {
            entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            entity.remove();
            event.setCancelled(true);
        }
    }
}
