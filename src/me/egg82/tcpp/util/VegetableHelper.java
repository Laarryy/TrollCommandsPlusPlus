package me.egg82.tcpp.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.services.VegetableItemRegistry;
import me.egg82.tcpp.services.VegetableModeRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.BlockUtil;

public class VegetableHelper {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry vegetableItemRegistry = (IRegistry) ServiceLocator.getService(VegetableItemRegistry.class);
	private IRegistry vegetableModeRegistry = (IRegistry) ServiceLocator.getService(VegetableModeRegistry.class);
	
	//constructor
	public VegetableHelper() {
		
	}
	
	//public
	public void vegetable(String uuid, Player player, Material itemType) {
		Location playerLocation = player.getLocation().clone();
		ItemStack groundItemStack = new ItemStack(itemType, 1);
		ItemMeta meta = groundItemStack.getItemMeta();
		meta.setDisplayName(player.getDisplayName());
		groundItemStack.setItemMeta(meta);
		
		Item groundItem = playerLocation.getWorld().dropItem(playerLocation, groundItemStack);
		Location groundItemLocation = groundItem.getLocation();
		
		vegetableItemRegistry.setRegister(uuid, Item.class, groundItem);
		vegetableModeRegistry.setRegister(uuid, GameMode.class, player.getGameMode());
		
		player.setGameMode(GameMode.SPECTATOR);
		player.teleport(new Location(groundItemLocation.getWorld(), groundItemLocation.getX(), groundItemLocation.getY() - 1.0d, groundItemLocation.getZ()));
		
		vegetableRegistry.setRegister(uuid, Player.class, player);
	}
	public void unvegetable(String uuid, Player player) {
		Location playerLocation = player.getLocation().clone();
		Item groundItem = (Item) vegetableItemRegistry.getRegister(uuid);
		GameMode oldMode = (GameMode) vegetableModeRegistry.getRegister(uuid);
		
		vegetableRegistry.setRegister(uuid, Player.class, null);
		vegetableItemRegistry.setRegister(uuid, Item.class, null);
		vegetableModeRegistry.setRegister(uuid, GameMode.class, null);
		
		groundItem.remove();
		player.teleport(BlockUtil.getTopWalkableBlock(playerLocation));
		player.setGameMode(oldMode);
	}
	
	public void unvegetableAll() {
		String[] names = vegetableRegistry.getRegistryNames();
		for (int i = 0; i < names.length; i++) {
			unvegetable(names[i], (Player) vegetableRegistry.getRegister(names[i]));
		}
	}
	
	//private
	
}
