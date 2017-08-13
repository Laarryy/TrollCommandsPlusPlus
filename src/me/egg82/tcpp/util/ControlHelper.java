package me.egg82.tcpp.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.services.ControlInventoryRegistry;
import me.egg82.tcpp.services.ControlModeRegistry;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class ControlHelper {
	//vars
	private IRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	private IRegistry<UUID> controlModeRegistry = ServiceLocator.getService(ControlModeRegistry.class);
	private IRegistry<UUID> controlInventoryRegistry = ServiceLocator.getService(ControlInventoryRegistry.class);
	
	private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
	
	//constructor
	public ControlHelper() {
		
	}
	
	//public
	public void control(UUID controllerUuid, Player controller, UUID playerUuid, Player player) {
		PlayerInventory controllerInventory = controller.getInventory();
		PlayerInventory playerInventory = player.getInventory();
		
		disguiseHelper.disguiseAsPlayer(controller, player);
		
		controlRegistry.setRegister(controllerUuid, playerUuid);
		controlModeRegistry.setRegister(controllerUuid, controller.getGameMode());
		controlInventoryRegistry.setRegister(controllerUuid, controllerInventory.getContents());
		
		controllerInventory.setContents(playerInventory.getContents());
		controller.setGameMode(player.getGameMode());
		
		playerInventory.clear();
		player.setGameMode(GameMode.SPECTATOR);
		
		controller.teleport(player);
		
		controller.sendMessage("You are now controlling " + player.getName() + "!");
		player.sendMessage("You are now being controlled!");
	}
	public void uncontrol(UUID controllerUuid, Player controller) {
		uncontrol(controllerUuid, controller, true);
	}
	public void uncontrol(UUID controllerUuid, Player controller, boolean vanish) {
		Player player = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(controllerUuid, UUID.class));
		
		PlayerInventory controllerInventory = controller.getInventory();
		PlayerInventory playerInventory = player.getInventory();
		
		if (vanish) {
			// Make controller invisible
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.hidePlayer(controller);
			}
			// Wait 10 seconds
			TaskUtil.runSync(new Runnable() {
				public void run() {
					// Make controller visible
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						p.showPlayer(controller);
					}
				}
			}, 200L);
		}
		
		disguiseHelper.undisguise(controller);
		
		player.teleport(controller);
		
		playerInventory.setContents(controllerInventory.getContents());
		player.setGameMode(controller.getGameMode());
		
		controllerInventory.setContents(controlInventoryRegistry.getRegister(controllerUuid, ItemStack[].class));
		controller.setGameMode(controlModeRegistry.getRegister(controllerUuid, GameMode.class));
		
		controlModeRegistry.removeRegister(controllerUuid);
		controlInventoryRegistry.removeRegister(controllerUuid);
		controlRegistry.removeRegister(controllerUuid);
		
		controller.sendMessage("You are no longer controlling " + player.getName() + ".");
		player.sendMessage("You are no longer being controlled.");
	}
	
	public void uncontrolAll() {
		for (UUID key : controlRegistry.getKeys()) {
			uncontrol(key, CommandUtil.getPlayerByUuid(key), false);
		}
	}
	
	//private
	
}
