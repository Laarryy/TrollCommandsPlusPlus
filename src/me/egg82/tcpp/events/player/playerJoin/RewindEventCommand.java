package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.RewindRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class RewindEventCommand extends EventCommand {
	//vars
	private IRegistry rewindRegistry = (IRegistry) ServiceLocator.getService(RewindRegistry.class);
	
	//constructor
	public RewindEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (rewindRegistry.hasRegister(uuid)) {
			rewindRegistry.setRegister(uuid, Player.class, player);
		}
	}
}