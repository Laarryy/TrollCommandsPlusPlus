package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.KillRegistry;
import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class KillEventCommand extends EventHandler<PlayerDeathEvent> {
    //vars
    private IVariableRegistry<UUID> killRegistry = ServiceLocator.getService(KillRegistry.class);

    //constructor
    public KillEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        killRegistry.removeRegister(event.getEntity().getUniqueId());
    }
}
