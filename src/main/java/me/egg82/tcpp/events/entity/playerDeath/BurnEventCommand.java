package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.BurnRegistry;
import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class BurnEventCommand extends EventHandler<PlayerDeathEvent> {
    //vars
    private IVariableRegistry<UUID> burnRegistry = ServiceLocator.getService(BurnRegistry.class);

    //constructor
    public BurnEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        burnRegistry.removeRegister(event.getEntity().getUniqueId());
    }
}
