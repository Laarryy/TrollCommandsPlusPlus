package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.ElectrifyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ElectrifyEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	IRegistry electrifyRegistry = ServiceLocator.getService(ElectrifyRegistry.class);
	
	//constructor
	public ElectrifyEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (electrifyRegistry.hasRegister(uuid)) {
			electrifyRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
