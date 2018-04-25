package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.registries.FreezeRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class FreezeEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IVariableRegistry<UUID> freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (freezeRegistry.hasRegister(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
