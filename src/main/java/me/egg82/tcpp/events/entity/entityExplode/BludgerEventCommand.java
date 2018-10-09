package me.egg82.tcpp.events.entity.entityExplode;

import java.util.UUID;

import me.egg82.tcpp.registries.BludgerRegistry;
import org.bukkit.event.entity.EntityExplodeEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class BludgerEventCommand extends EventHandler<EntityExplodeEvent> {
    //vars
    private IVariableRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);

    //constructor
    public BludgerEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        UUID uuid = bludgerRegistry.getKey(event.getEntity());
        if (uuid != null) {
            bludgerRegistry.removeRegister(uuid);
        }
    }
}
