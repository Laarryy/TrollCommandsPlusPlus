package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.LavaBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LavaBuildEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry lavaBuildRegistry = ServiceLocator.getService(LavaBuildRegistry.class);
	
	//constructor
	public LavaBuildEventCommand(BlockPlaceEvent event) {
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
		
		if (lavaBuildRegistry.hasRegister(uuid)) {
			event.getBlock().setType(Material.LAVA);
			lavaBuildRegistry.setRegister(uuid, Player.class, null);
		}
	}
}
