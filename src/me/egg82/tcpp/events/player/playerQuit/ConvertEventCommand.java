package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.ConvertRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ConvertEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> convertRegistry = ServiceLocator.getService(ConvertRegistry.class);
	
	//constructor
	public ConvertEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		convertRegistry.removeRegister(event.getPlayer().getUniqueId());
	}
}
