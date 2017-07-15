package me.egg82.tcpp.events.block.blockBreak;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.services.ExplodeBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ExplodeBreakEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private IRegistry explodeBreakRegistry = ServiceLocator.getService(ExplodeBreakRegistry.class);
	
	//constructor
	public ExplodeBreakEventCommand(BlockBreakEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (explodeBreakRegistry.hasRegister(uuid)) {
			Location blockLocation = event.getBlock().getLocation();
			blockLocation.getWorld().createExplosion(blockLocation.getX() + 0.5d, blockLocation.getY() + 0.5d, blockLocation.getZ() + 0.5d, 4.0f, true, true);
			explodeBreakRegistry.setRegister(uuid, Player.class, null);
		}
	}
}
