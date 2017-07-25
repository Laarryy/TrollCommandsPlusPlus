package me.egg82.tcpp.util;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.services.VegetableItemRegistry;
import me.egg82.tcpp.services.VegetableLocationRegistry;
import me.egg82.tcpp.services.VegetableModeRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;

public class VegetableHelper {
	//vars
	private IRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry<UUID> vegetableItemRegistry = ServiceLocator.getService(VegetableItemRegistry.class);
	private IRegistry<UUID> vegetableModeRegistry = ServiceLocator.getService(VegetableModeRegistry.class);
	private IRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
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
