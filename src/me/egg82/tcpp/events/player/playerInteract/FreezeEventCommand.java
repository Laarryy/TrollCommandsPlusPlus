package me.egg82.tcpp.events.player.playerInteract;

import java.util.UUID;

import org.bukkit.event.player.PlayerInteractEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand<PlayerInteractEvent> {
	//vars
	private IRegistry<UUID> freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(PlayerInteractEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (freezeRegistry.hasRegister(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
