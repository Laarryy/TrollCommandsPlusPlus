package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.ElectrifyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ElectrifyEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	IRegistry electrifyRegistry = ServiceLocator.getService(ElectrifyRegistry.class);
	
	//constructor
	public ElectrifyEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		electrifyRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
