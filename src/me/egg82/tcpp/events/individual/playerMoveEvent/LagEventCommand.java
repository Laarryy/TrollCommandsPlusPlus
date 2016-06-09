package me.egg82.tcpp.events.individual.playerMoveEvent;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_REGISTRY);
	private IRegistry lagInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_INTERN_REGISTRY);
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		Location to = e.getTo();
		Player player = e.getPlayer();
		String name = player.getName().toLowerCase();
		
		if (lagRegistry.contains(name)) {
			HashMap<String, Object> map = (HashMap<String, Object>) lagInternRegistry.getRegister(name);
			long lastLoc = (long) map.get("lastLoc");
			
			if (System.currentTimeMillis() - lastLoc >= 500 && Math.random() <= 0.05d) {
				Location from = (Location) map.get("loc");
				map.put("lastLoc", System.currentTimeMillis());
				to.setX(from.getX());
				to.setY(from.getY());
				to.setZ(from.getZ());
				to.setDirection(from.getDirection());
			}
			if (Math.random() <= 0.15d) {
				map.put("loc", player.getLocation());
			}
		}
	}
}
