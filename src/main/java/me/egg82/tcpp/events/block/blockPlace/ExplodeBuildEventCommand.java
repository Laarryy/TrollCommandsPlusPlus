package me.egg82.tcpp.events.block.blockPlace;

import java.util.UUID;

import me.egg82.tcpp.registries.ExplodeBuildRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ExplodeBuildEventCommand extends EventHandler<BlockPlaceEvent> {
    //vars
    private IVariableRegistry<UUID> explodeBuildRegistry = ServiceLocator.getService(ExplodeBuildRegistry.class);

    //constructor
    public ExplodeBuildEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (explodeBuildRegistry.hasRegister(uuid)) {
            Location blockLocation = event.getBlock().getLocation();
            blockLocation.getWorld().createExplosion(blockLocation.getX() + 0.5d, blockLocation.getY() + 0.5d, blockLocation.getZ() + 0.5d, 4.0f, true, true);
            explodeBuildRegistry.removeRegister(uuid);
        }
    }
}
