package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.NauseaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NauseaEventCommand extends EventCommand {
	//vars
	IRegistry nauseaRegistry = (IRegistry) ServiceLocator.getService(NauseaRegistry.class);
	
	//constructor
	public NauseaEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (nauseaRegistry.hasRegister(uuid)) {
			nauseaRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
