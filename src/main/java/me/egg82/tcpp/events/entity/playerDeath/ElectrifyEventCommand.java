package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.ElectrifyRegistry;
import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ElectrifyEventCommand extends EventHandler<PlayerDeathEvent> {
    //vars
    private IVariableRegistry<UUID> electrifyRegistry = ServiceLocator.getService(ElectrifyRegistry.class);

    //constructor
    public ElectrifyEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        electrifyRegistry.removeRegister(event.getEntity().getUniqueId());
    }
}
