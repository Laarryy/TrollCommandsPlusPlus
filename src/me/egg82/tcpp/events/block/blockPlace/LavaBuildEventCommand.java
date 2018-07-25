package me.egg82.tcpp.events.block.blockPlace;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.registries.LavaBuildRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class LavaBuildEventCommand extends EventHandler<BlockPlaceEvent> {
	//vars
	private IVariableRegistry<UUID> lavaBuildRegistry = ServiceLocator.getService(LavaBuildRegistry.class);
	
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
