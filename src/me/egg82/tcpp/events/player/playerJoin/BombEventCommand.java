package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.BombRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BombEventCommand extends EventCommand {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(BombRegistry.class);
	
	//constructor
	public BombEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (bombRegistry.hasRegister(uuid)) {
			bombRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
