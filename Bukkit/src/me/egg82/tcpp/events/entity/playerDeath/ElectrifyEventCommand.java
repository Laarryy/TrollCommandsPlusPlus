package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.ElectrifyRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class ElectrifyEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IVariableRegistry<UUID> electrifyRegistry = ServiceLocator.getService(ElectrifyRegistry.class);
	
	//constructor
	public ElectrifyEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		electrifyRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
