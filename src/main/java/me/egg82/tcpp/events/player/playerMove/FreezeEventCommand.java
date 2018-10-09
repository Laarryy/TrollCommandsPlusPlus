package me.egg82.tcpp.events.player.playerMove;

import java.util.UUID;

import me.egg82.tcpp.registries.FreezeRegistry;
import org.bukkit.event.player.PlayerMoveEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class FreezeEventCommand extends EventHandler<PlayerMoveEvent> {
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
            if (event.getTo().getY() < event.getFrom().getY()) {
                event.getTo().setX(event.getFrom().getX());
                event.getTo().setZ(event.getFrom().getZ());
            } else {
                event.setCancelled(true);
            }
        }
    }
}
