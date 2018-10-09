package me.egg82.tcpp.events.block.blockBreak;

import java.util.UUID;

import me.egg82.tcpp.registries.ExplodeBreakRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ExplodeBreakEventCommand extends EventHandler<BlockBreakEvent> {
    //vars
    private IVariableRegistry<UUID> explodeBreakRegistry = ServiceLocator.getService(ExplodeBreakRegistry.class);

    //constructor
    public ExplodeBreakEventCommand() {
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

        if (explodeBreakRegistry.hasRegister(uuid)) {
            Location blockLocation = event.getBlock().getLocation();
            blockLocation.getWorld().createExplosion(blockLocation.getX() + 0.5d, blockLocation.getY() + 0.5d, blockLocation.getZ() + 0.5d, 4.0f, true, true);
            explodeBreakRegistry.removeRegister(uuid);
        }
    }
}
