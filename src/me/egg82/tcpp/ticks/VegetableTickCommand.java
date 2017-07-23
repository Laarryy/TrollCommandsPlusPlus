package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableItemRegistry;
import me.egg82.tcpp.services.VegetableLocationRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class VegetableTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry<UUID> vegetableItemRegistry = ServiceLocator.getService(VegetableItemRegistry.class);
	private IRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = vegetableRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
			return;
		}
		
		Item groundItem = vegetableItemRegistry.getRegister(uuid, Item.class);
		Location oldItemLocation = vegetableLocationRegistry.getRegister(uuid, Location.class);
		
		Location playerLocation = player.getEyeLocation();
		Location newItemLocation = groundItem.getLocation();
		
		newItemLocation.setDirection(playerLocation.getDirection());
		
		if (!LocationUtil.areEqualXYZ(oldItemLocation, newItemLocation, 0.25d)) {
			vegetableLocationRegistry.setRegister(uuid, newItemLocation.clone());
			player.teleport(LocationUtil.makeEqualXYZ(newItemLocation.clone().add(0.0d, -1.0d, 0.0d), playerLocation));
		}
	}
}
