package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.ExplodeBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ExplodeBuildEventCommand extends EventCommand {
	//vars
	private IRegistry explodeBuildRegistry = (IRegistry) ServiceLocator.getService(ExplodeBuildRegistry.class);

	//constructor
	public ExplodeBuildEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (explodeBuildRegistry.hasRegister(uuid)) {
			Location blockLocation = e.getBlock().getLocation();
			blockLocation.getWorld().createExplosion(blockLocation.getX() + 0.5d, blockLocation.getY() + 0.5d, blockLocation.getZ() + 0.5d, 4.0f, true, true);
			explodeBuildRegistry.setRegister(uuid, Player.class, null);
		}
	}
}
