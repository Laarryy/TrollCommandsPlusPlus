package me.egg82.tcpp.events.player.playerQuit;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.DisplayBlockRegistry;
import me.egg82.tcpp.services.DisplayLocationRegistry;
import me.egg82.tcpp.services.DisplayRegistry;
import me.egg82.tcpp.util.DisplayHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand {
	//vars
	private IRegistry displayRegistry = (IRegistry) ServiceLocator.getService(DisplayRegistry.class);
	private IRegistry displayBlockRegistry = (IRegistry) ServiceLocator.getService(DisplayBlockRegistry.class);
	private IRegistry displayLocationRegistry = (IRegistry) ServiceLocator.getService(DisplayLocationRegistry.class);
	
	private DisplayHelper displayHelper = (DisplayHelper) ServiceLocator.getService(DisplayHelper.class);
	
	//constructor
	public DisplayEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerQuitEvent e = (PlayerQuitEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!displayRegistry.hasRegister(uuid)) {
			return;
		}
		
		displayHelper.unsurround(player.getLocation());
		displayRegistry.setRegister(uuid, Player.class, null);
		displayBlockRegistry.setRegister(uuid, Set.class, null);
		displayLocationRegistry.setRegister(uuid, Location.class, null);
	}
}
