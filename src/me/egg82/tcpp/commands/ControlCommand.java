package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.util.interfaces.IDisguiseHelper;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class ControlCommand extends BasePluginCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
	private IRegistry controllerRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
	private IRegistry controlDataRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_DATA_REGISTRY);
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(PluginServiceType.DISGUISE_HELPER);
	
	//constructor
	public ControlCommand() {
		super();
	}
	
	//public
	public void onQuit(String uuid, Player player) {
		controlRegistry.computeIfPresent(uuid, (k,v) -> {
			return null;
		});
		
		controllerRegistry.computeIfPresent(uuid, (k,v) -> {
			Player p = (Player) v;
			
			if (p != null) {
				p.kickPlayer("You were being controlled, and your controller quit/was kicked.");
			}
			
			controlDataRegistry.setRegister(uuid, null);
			return null;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(true, PermissionsType.COMMAND_CONTROL, new int[]{0,1}, new int[]{0})) {
			if (!disguiseHelper.isValidLibrary()) {
				sender.sendMessage(MessageType.NO_LIBRARY);
				dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
				return;
			}
			
			Player p = (Player) sender;
			Player player = null;
			
			String pUuid = p.getUniqueId().toString();
			String playerUuid = null;
			
			if (args.length == 0) {
				if (controllerRegistry.contains(pUuid)) {
					player = (Player) controllerRegistry.getRegister(pUuid);
					
					if (player != null) {
						playerUuid = player.getUniqueId().toString();
						e(pUuid, p, playerUuid, player);
					} else {
						e(pUuid, p, "Anyone", null);
					}
				} else {
					sender.sendMessage(MessageType.NOT_CONTROLLING);
					dispatch(CommandEvent.ERROR, CommandErrorType.NOT_CONTROLLING);
					return;
				}
			} else if (args.length == 1) {
				player = Bukkit.getPlayer(args[0]);
				playerUuid = player.getUniqueId().toString();
				
				if (pUuid == playerUuid) {
					sender.sendMessage(MessageType.NO_CONTROL_SELF);
					dispatch(CommandEvent.ERROR, CommandErrorType.NO_CONTROL_SELF);
					return;
				}
				e(pUuid, p, playerUuid, player);
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String controllerUuid, Player controller, String uuid, Player player) {
		PlayerInventory controllerInv = controller.getInventory();
		HashMap<String, Object> map = null;
		PlayerInventory playerInv = null;
		
		if (player != null) {
			playerInv = player.getInventory();
		}
		
		if (controllerRegistry.contains(controllerUuid)) {
			sender.sendMessage("You are no longer controlling " + player.getName() + ".");
			if (player != null) {
				player.sendMessage("You are no longer being controlled.");
			}
			
			controller.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 3), true);
			
			if (playerInv != null) {
				playerInv.setContents(controllerInv.getContents());
			}
			map = (HashMap<String, Object>) controlDataRegistry.getRegister(uuid);
			if (player != null) {
				player.setGameMode((GameMode) map.get("mode"));
				player.teleport(controller);
			}
			if (map != null) {
				controllerInv.setContents((ItemStack[]) map.get("inventory"));
				controller.teleport((Location) map.get("location"));
			}
			
			disguiseHelper.undisguise(controller);
			
			controlRegistry.setRegister(uuid, null);
			controllerRegistry.setRegister(controllerUuid, null);
			controlDataRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage("You are now controlling " + player.getName() + "!");
			player.sendMessage("You are now being controlled!");
			
			controlRegistry.setRegister(uuid, player);
			controllerRegistry.setRegister(controllerUuid, player);
			
			map = new HashMap<String, Object>();
			map.put("inventory", controllerInv.getContents());
			map.put("location", controller.getLocation());
			map.put("mode", player.getGameMode());
			controlDataRegistry.setRegister(uuid, map);
			
			controllerInv.setContents(playerInv.getContents());
			controller.teleport(player);
			player.setGameMode(GameMode.SPECTATOR);
			
			disguiseHelper.disguiseAsPlayer(controller, player);
		}
	}
}
