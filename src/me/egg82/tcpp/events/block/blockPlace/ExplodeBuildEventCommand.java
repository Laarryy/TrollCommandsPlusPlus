package me.egg82.tcpp.events.block.blockPlace;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.registries.ExplodeBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ExplodeBuildEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry<UUID> explodeBuildRegistry = ServiceLocator.getService(ExplodeBuildRegistry.class);

	//constructor
	public ExplodeBuildEventCommand(BlockPlaceEvent event) {
		super(event);
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
