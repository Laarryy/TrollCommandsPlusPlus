package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import me.egg82.tcpp.registries.FreezeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class FreezeEventCommand extends EventHandler<PlayerPickupArrowEvent> {
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

        Player player = event.getPlayer();

        if (freezeRegistry.hasRegister(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
