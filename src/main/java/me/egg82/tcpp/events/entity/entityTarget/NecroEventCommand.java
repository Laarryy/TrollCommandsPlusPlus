package me.egg82.tcpp.events.entity.entityTarget;

import java.util.UUID;

import me.egg82.tcpp.registries.NecroRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class NecroEventCommand extends EventHandler<EntityTargetEvent> {
    //vars
    private IVariableRegistry<UUID> necroRegistry = ServiceLocator.getService(NecroRegistry.class);

    //constructor
    public NecroEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Entity target = event.getTarget();

        if (target == null) {
            return;
        }

        if (necroRegistry.hasRegister(target.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
