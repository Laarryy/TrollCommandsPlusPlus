package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import me.egg82.tcpp.registries.SpartaArrowRegistry;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class SpartaEventCommand extends EventHandler<PlayerPickupArrowEvent> {
    //vars
    private IVariableRegistry<UUID> spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);

    //constructor
    public SpartaEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        if (spartaArrowRegistry.hasRegister(event.getArrow().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
