package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.services.InfinityRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.LocationUtil;

public class InfinityEventCommand extends EventCommand {
	//vars
	private IRegistry infinityRegistry = (IRegistry) ServiceLocator.getService(InfinityRegistry.class);
	
	//constructor
	public InfinityEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerTeleportEvent e = (PlayerTeleportEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (!infinityRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		Location toLocation = e.getTo().clone();
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
			player.teleport(LocationUtil.makeEqualXYZ(toLocation, e.getTo()));
		}
	}
}
