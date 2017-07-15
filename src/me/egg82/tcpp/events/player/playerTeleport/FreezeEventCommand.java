package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand<PlayerTeleportEvent> {
	//vars
	private IRegistry freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(PlayerTeleportEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (freezeRegistry.hasRegister(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}
}
