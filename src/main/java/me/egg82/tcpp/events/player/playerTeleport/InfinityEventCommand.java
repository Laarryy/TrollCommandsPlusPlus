package me.egg82.tcpp.events.player.playerTeleport;

import java.util.UUID;

import me.egg82.tcpp.registries.InfinityRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class InfinityEventCommand extends EventHandler<PlayerTeleportEvent> {
    //vars
    private IVariableRegistry<UUID> infinityRegistry = ServiceLocator.getService(InfinityRegistry.class);

    //constructor
    public InfinityEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        if (!infinityRegistry.hasRegister(player.getUniqueId())) {
            return;
        }

        Location toLocation = event.getTo().clone();
        World world = toLocation.getWorld();
        double highestY = 0.0d;

        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                double tempY = world.getHighestBlockAt(toLocation.clone().add(x, 0.0d, z)).getLocation().getBlockY();

                if (tempY > highestY) {
                    highestY = tempY;
                }
            }
        }

        if (toLocation.getY() <= highestY + 2.0d) {
            toLocation.add(0.0d, 30.0d, 0.0d);
            player.teleport(LocationUtil.makeEqualXYZ(toLocation, event.getTo()));
        }
    }
}
