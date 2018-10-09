package me.egg82.tcpp.events.block.blockBreak;

import java.util.Set;
import java.util.UUID;

import me.egg82.tcpp.registries.DisplayRegistry;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class DisplayEventCommand extends EventHandler<BlockBreakEvent> {
    //vars
    private IVariableRegistry<UUID> displayRegistry = ServiceLocator.getService(DisplayRegistry.class);

    //constructor
    public DisplayEventCommand() {
        super();
    }

    //public

    //private
    @SuppressWarnings("unchecked")
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Location loc = event.getBlock().getLocation();

        for (UUID key : displayRegistry.getKeys()) {
            Set<Location> blockedLocs = displayRegistry.getRegister(key, Set.class);

            if (blockedLocs.contains(loc)) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
