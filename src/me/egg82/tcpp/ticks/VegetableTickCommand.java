package me.egg82.tcpp.ticks;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class VegetableTickCommand extends TickCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
	private IRegistry vegetableInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_INTERN_REGISTRY);
	
	//constructor
	public VegetableTickCommand() {
		super();
		ticks = 5l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = vegetableRegistry.registryNames();
		for (String name : names) {
			e(name, (Player) vegetableRegistry.getRegister(name));
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String uuid, Player player) {
		if (player == null) {
			return;
		}
		
		HashMap<String, Object> map = (HashMap<String, Object>) vegetableInternRegistry.getRegister(uuid);
		Item potato = (Item) map.get("item");
		
		if (potato == null) {
			return;
		}
		
		Location loc = player.getLocation();
		Location loc2 = potato.getLocation().clone().subtract(0.0d, 1.0d, 0.0d);
		loc2.setDirection(loc.getDirection());
		player.teleport(loc2);
	}
}
