package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableItemRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class VegetableTickCommand extends TickCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry vegetableItemRegistry = (IRegistry) ServiceLocator.getService(VegetableItemRegistry.class);
	
	//constructor
	public VegetableTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = vegetableRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) vegetableRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
			return;
		}
		
		Item groundItem = (Item) vegetableItemRegistry.getRegister(uuid);
		
		Location playerLocation = player.getLocation().clone().add(0.0d, 1.0d, 0.0d);
		Location itemLocation = groundItem.getLocation().clone();
		
		itemLocation.setDirection(playerLocation.getDirection());
		player.setVelocity(playerLocation.subtract(itemLocation).toVector().normalize().multiply(1.0d));
	}
}
