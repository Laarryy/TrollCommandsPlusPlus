package me.egg82.tcpp.events.player.playerPickupArrow;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.NoPickupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NoPickupEventCommand extends EventCommand {
	//vars
	private IRegistry noPickupRegistry = (IRegistry) ServiceLocator.getService(NoPickupRegistry.class);
	
	//constructor
	public NoPickupEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerPickupArrowEvent e = (PlayerPickupArrowEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (noPickupRegistry.hasRegister(player.getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}
}
