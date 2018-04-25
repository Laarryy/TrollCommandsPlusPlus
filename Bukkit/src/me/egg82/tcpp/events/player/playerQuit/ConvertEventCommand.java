package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.ConvertRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class ConvertEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IVariableRegistry<UUID> convertRegistry = ServiceLocator.getService(ConvertRegistry.class);
	
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
