package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.registries.VegetableItemRegistry;
import me.egg82.tcpp.services.registries.VegetableLocationRegistry;
import me.egg82.tcpp.services.registries.VegetableRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class VegetableTickCommand extends TickCommand {
	//vars
	private IVariableRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IVariableRegistry<UUID> vegetableItemRegistry = ServiceLocator.getService(VegetableItemRegistry.class);
	private IVariableRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableTickCommand() {
		super(2L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : vegetableRegistry.getKeys()) {
			e(key, CommandUtil.getPlayerByUuid(key), vegetableItemRegistry.getRegister(key, Item.class), vegetableLocationRegistry.getRegister(key, Location.class));
		}
	}
	private void e(UUID uuid, Player player, Item groundItem, Location oldItemLocation) {
		if (player == null) {
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
			return;
		}
		
		Location playerLocation = player.getEyeLocation();
		Location newItemLocation = groundItem.getLocation();
		
		newItemLocation.setDirection(playerLocation.getDirection());
		
		if (!LocationUtil.areEqualXYZ(oldItemLocation, newItemLocation, 0.25d)) {
			vegetableLocationRegistry.setRegister(uuid, newItemLocation.clone());
			player.teleport(LocationUtil.makeEqualXYZ(newItemLocation.clone().add(0.0d, -1.0d, 0.0d), playerLocation));
		}
	}
}
