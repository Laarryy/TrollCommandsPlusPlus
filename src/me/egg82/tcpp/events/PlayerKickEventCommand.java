package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerKickEvent;

import me.egg82.tcpp.util.RegistryUtil;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerKickEventCommand extends EventCommand {
	//vars
	
	//constructor
	public PlayerKickEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerKickEvent e = (PlayerKickEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		RegistryUtil.onQuit(player.getName().toLowerCase(), player);
	}
}
