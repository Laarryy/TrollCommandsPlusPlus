package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IRegistry<UUID> freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(PlayerPickupArrowEvent event) {
		super(event);
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
