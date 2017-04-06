package me.egg82.tcpp.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.ControlInventoryRegistry;
import me.egg82.tcpp.services.ControlModeRegistry;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;

public class ControlHelper {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	private IRegistry controlModeRegistry = (IRegistry) ServiceLocator.getService(ControlModeRegistry.class);
	private IRegistry controlInventoryRegistry = (IRegistry) ServiceLocator.getService(ControlInventoryRegistry.class);
	
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(IDisguiseHelper.class);
	
	//constructor
	public ControlHelper() {
		
	}
	
	//public
	public void control(String controllerUuid, Player controller, String uuid, Player player) {
		PlayerInventory controllerInventory = controller.getInventory();
		PlayerInventory playerInventory = player.getInventory();
		
		disguiseHelper.disguiseAsPlayer(controller, player);
		
		controlRegistry.setRegister(controllerUuid, Player.class, player);
		controlModeRegistry.setRegister(controllerUuid, GameMode.class, controller.getGameMode());
		controlInventoryRegistry.setRegister(controllerUuid, ItemStack[].class, controllerInventory.getContents());
		
		controllerInventory.setContents(playerInventory.getContents());
		controller.setGameMode(player.getGameMode());
		
		playerInventory.clear();
		player.setGameMode(GameMode.SPECTATOR);
		
		controller.teleport(player);
		
		controller.sendMessage("You are now controlling " + player.getName() + "!");
		player.sendMessage("You are now being controlled!");
	}
	public void uncontrol(String controllerUuid, Player controller) {
		Player player = (Player) controlRegistry.getRegister(controllerUuid);
		
		PlayerInventory controllerInventory = controller.getInventory();
		PlayerInventory playerInventory = player.getInventory();
		
		controller.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 3), true);
		disguiseHelper.undisguise(controller);
		
		player.teleport(controller);
		
		playerInventory.setContents(controllerInventory.getContents());
		player.setGameMode(controller.getGameMode());
		
		controllerInventory.setContents((ItemStack[]) controlInventoryRegistry.getRegister(controllerUuid));
		controller.setGameMode((GameMode) controlModeRegistry.getRegister(controllerUuid));
		
		controlModeRegistry.setRegister(controllerUuid, GameMode.class, null);
		controlInventoryRegistry.setRegister(controllerUuid, ItemStack[].class, null);
		controlRegistry.setRegister(controllerUuid, Player.class, null);
		
		controller.sendMessage("You are no longer controlling " + player.getName() + ".");
		player.sendMessage("You are no longer being controlled.");
	}
	
	//private
	
}
