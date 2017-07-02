package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand {
	//vars
	private IRegistry freezeRegistry = (IRegistry) ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerTeleportEvent e = (PlayerTeleportEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (freezeRegistry.hasRegister(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}
}
