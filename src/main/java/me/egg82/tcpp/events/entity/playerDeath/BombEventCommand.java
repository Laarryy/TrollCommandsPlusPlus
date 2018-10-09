package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.BombRegistry;
import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class BombEventCommand extends EventHandler<PlayerDeathEvent> {
    //vars
    private IVariableRegistry<UUID> bombRegistry = ServiceLocator.getService(BombRegistry.class);

    //constructor
    public BombEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        bombRegistry.removeRegister(event.getEntity().getUniqueId());
    }
}
