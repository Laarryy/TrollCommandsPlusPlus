package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.util.RegistryUtil;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerDeathEventCommand extends EventCommand {
	//vars
	
	//constructor
	public PlayerDeathEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		Player player = ((PlayerDeathEvent) event).getEntity();
		RegistryUtil.onDeath(player.getName().toLowerCase(), player);
	}
}