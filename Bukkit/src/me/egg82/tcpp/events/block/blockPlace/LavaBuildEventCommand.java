package me.egg82.tcpp.events.block.blockPlace;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.registries.LavaBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LavaBuildEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry<UUID> lavaBuildRegistry = ServiceLocator.getService(LavaBuildRegistry.class);
	
	//constructor
	public LavaBuildEventCommand() {
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
		
		if (lavaBuildRegistry.hasRegister(uuid)) {
			event.getBlock().setType(Material.LAVA);
			lavaBuildRegistry.removeRegister(uuid);
		}
	}
}
