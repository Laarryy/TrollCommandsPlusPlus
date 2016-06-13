package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerLoginEvent;

import me.egg82.tcpp.util.RegistryUtil;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerLoginEventCommand extends EventCommand {
	//vars
	
	//constructor
	public PlayerLoginEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		Player player = ((PlayerLoginEvent) event).getPlayer();
		RegistryUtil.onLogin(player.getName().toLowerCase(), player);
	}
}
