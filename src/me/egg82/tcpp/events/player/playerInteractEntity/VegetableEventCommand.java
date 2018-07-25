package me.egg82.tcpp.events.player.playerInteractEntity;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.VegetableRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class VegetableEventCommand extends EventHandler<PlayerInteractEntityEvent> {
	//vars
	private IVariableRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (vegetableRegistry.hasRegister(event.getPlayer().getUniqueId())) {
			if (!player.hasPermission(PermissionsType.FREECAM_WHILE_VEGETABLE)) {
				event.setCancelled(true);
			}
		}
	}
}
