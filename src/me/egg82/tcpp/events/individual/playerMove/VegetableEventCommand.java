package me.egg82.tcpp.events.individual.playerMove;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
	private IRegistry vegetableInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_INTERN_REGISTRY);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		String name = e.getPlayer().getName().toLowerCase();
		
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if (vegetableRegistry.contains(name)) {
			HashMap<String, Object> map = (HashMap<String, Object>) vegetableInternRegistry.getRegister(name);
			Item potato = (Item) map.get("item");
			
			if (potato == null) {
				if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
					e.setCancelled(true);
				}
				return;
			}
			
			Location p = potato.getLocation().clone().subtract(0.0d, 1.0d, 0.0d);
			
			if (to.getX() != p.getX() || to.getY() != p.getY() || to.getZ() != p.getZ()) {
				to.setX(p.getX());
				to.setY(p.getY());
				to.setZ(p.getZ());
			}
		}
	}
}
