package me.egg82.tcpp.events.player.playerInteractEntity;

import java.util.UUID;

import me.egg82.tcpp.registries.FreezeRegistry;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class FreezeEventCommand extends EventHandler<PlayerInteractEntityEvent> {
    //vars
    private IVariableRegistry<UUID> freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);

    //constructor
    public FreezeEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        if (freezeRegistry.hasRegister(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
