package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.util.RegistryUtil;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerQuitEventCommand extends EventCommand {
	//vars
	
	//constructor
	public PlayerQuitEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		Player player = ((PlayerQuitEvent) event).getPlayer();
		RegistryUtil.onQuit(player.getName().toLowerCase(), player);
	}
}
