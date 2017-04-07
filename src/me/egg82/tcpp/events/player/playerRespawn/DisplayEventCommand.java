package me.egg82.tcpp.events.player.playerRespawn;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.DisplayLocationRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand {
	//vars
	private IRegistry displayLocationRegistry = (IRegistry) ServiceLocator.getService(DisplayLocationRegistry.class);
	
	//constructor
	public DisplayEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerRespawnEvent e = (PlayerRespawnEvent) event;
		
		Location loc = (Location) displayLocationRegistry.getRegister(e.getPlayer().getUniqueId().toString());
		if (loc != null) {
			e.setRespawnLocation(loc);
		}
	}
}
