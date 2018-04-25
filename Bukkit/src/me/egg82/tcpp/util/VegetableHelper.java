package me.egg82.tcpp.util;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.services.registries.VegetableItemRegistry;
import me.egg82.tcpp.services.registries.VegetableLocationRegistry;
import me.egg82.tcpp.services.registries.VegetableModeRegistry;
import me.egg82.tcpp.services.registries.VegetableRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;

public class VegetableHelper {
	//vars
	private IVariableRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IVariableRegistry<UUID> vegetableItemRegistry = ServiceLocator.getService(VegetableItemRegistry.class);
	private IVariableRegistry<UUID> vegetableModeRegistry = ServiceLocator.getService(VegetableModeRegistry.class);
	private IVariableRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableHelper() {
		
	}
	
	//public
	public void vegetable(UUID uuid, Player player, Material itemType) {
		Location playerLocation = player.getLocation().clone();
		ItemStack groundItemStack = new ItemStack(itemType, 1);
		ItemMeta meta = groundItemStack.getItemMeta();
		meta.setDisplayName(player.getDisplayName());
		groundItemStack.setItemMeta(meta);
		
		Item groundItem = playerLocation.getWorld().dropItem(playerLocation, groundItemStack);
		Location groundItemLocation = groundItem.getLocation();
		
		vegetableItemRegistry.setRegister(uuid, groundItem);
		vegetableModeRegistry.setRegister(uuid, player.getGameMode());
		vegetableLocationRegistry.setRegister(uuid, groundItemLocation);
		
		player.setGameMode(GameMode.SPECTATOR);
		player.teleport(new Location(groundItemLocation.getWorld(), groundItemLocation.getX(), groundItemLocation.getY() - 1.0d, groundItemLocation.getZ()));
		
		vegetableRegistry.setRegister(uuid, null);
	}
	public void unvegetable(UUID uuid, Player player) {
		Location playerLocation = player.getLocation().clone();
		Item groundItem = vegetableItemRegistry.getRegister(uuid, Item.class);
		GameMode oldMode = vegetableModeRegistry.getRegister(uuid, GameMode.class);
		
		vegetableRegistry.removeRegister(uuid);
		vegetableItemRegistry.removeRegister(uuid);
		vegetableModeRegistry.removeRegister(uuid);
		vegetableLocationRegistry.removeRegister(uuid);
		
		groundItem.remove();
		player.teleport(BlockUtil.getTopWalkableBlock(playerLocation));
		player.setGameMode(oldMode);
	}
	
	public void unvegetableAll() {
		for (UUID key : vegetableRegistry.getKeys()) {
			unvegetable(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	
	//private
	
}
