package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.BrittleRegistry;
import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class BrittleEventCommand extends EventHandler<PlayerDeathEvent> {
    //vars
    private IVariableRegistry<UUID> brittleRegistry = ServiceLocator.getService(BrittleRegistry.class);

    //constructor
    public BrittleEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        brittleRegistry.removeRegister(event.getEntity().getUniqueId());
    }
}
