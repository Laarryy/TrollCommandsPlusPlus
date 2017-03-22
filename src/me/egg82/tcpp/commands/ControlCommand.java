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
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
	private IRegistry reg3 = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_DATA_REGISTRY);
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(PluginServiceType.DISGUISE_HELPER);
	
	//constructor
	public ControlCommand() {
		super();
	}
	
	//public
	public void onQuit(String name, Player player) {
		reg.computeIfPresent(name, (k,v) -> {
			return null;
		});
		
		reg2.computeIfPresent(name, (k,v) -> {
			Player p = (Player) v;
			
			if (p != null) {
				p.kickPlayer("You were being controlled, and your controller quit/was kicked.");
			}
			
			reg3.setRegister(name, null);
			return null;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(true, PermissionsType.COMMAND_CONTROL, new int[]{0,1}, new int[]{0})) {
			Player p = (Player) sender;
			Player player = null;
			
			String pName = p.getName();
			String pLowerName = pName.toLowerCase();
			String playerName = null;
			String playerLowerName = null;
			
			if (args.length == 0) {
				if (reg2.contains(pLowerName)) {
					player = (Player) reg2.getRegister(pLowerName);
					
					if (player != null) {
						playerName = player.getName();
						playerLowerName = playerName.toLowerCase();
						e(pName, p, playerName, player);
					} else {
						e(pName, p, "Anyone", null);
					}
				} else {
					sender.sendMessage(MessageType.NOT_CONTROLLING);
					dispatch(CommandEvent.ERROR, CommandErrorType.NOT_CONTROLLING);
					return;
				}
			} else if (args.length == 1) {
				player = Bukkit.getPlayer(args[0]);
				playerName = player.getName();
				playerLowerName = playerName.toLowerCase();
				
				if (pLowerName == playerLowerName) {
					sender.sendMessage(MessageType.NO_CONTROL_SELF);
					dispatch(CommandEvent.ERROR, CommandErrorType.NO_CONTROL_SELF);
					return;
				}
				e(pName, p, playerName, player);
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String controllerName, Player controller, String name, Player player) {
		String controllerLowerName = controllerName.toLowerCase();
		String lowerName = name.toLowerCase();
		
		PlayerInventory controllerInv = controller.getInventory();
		HashMap<String, Object> map = null;
		PlayerInventory playerInv = null;
		
		if (player != null) {
			playerInv = player.getInventory();
		}
		
		if (reg2.contains(controllerName.toLowerCase())) {
			sender.sendMessage("You are no longer controlling " + name + ".");
			if (player != null) {
				player.sendMessage("You are no longer being controlled.");
			}
			
			controller.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 3), true);
			
			if (playerInv != null) {
				playerInv.setContents(controllerInv.getContents());
			}
			map = (HashMap<String, Object>) reg3.getRegister(lowerName);
			if (player != null) {
				player.setGameMode((GameMode) map.get("mode"));
				player.teleport(controller);
			}
			if (map != null) {
				controllerInv.setContents((ItemStack[]) map.get("inventory"));
				controller.teleport((Location) map.get("location"));
			}
			
			disguiseHelper.undisguise(controller);
			
			reg.setRegister(lowerName, null);
			reg2.setRegister(controllerLowerName, null);
			reg3.setRegister(lowerName, null);
		} else {
			sender.sendMessage("You are now controlling " + name + "!");
			player.sendMessage("You are now being controlled!");
			
			reg.setRegister(lowerName, player);
			reg2.setRegister(controllerLowerName, player);
			
			map = new HashMap<String, Object>();
			map.put("inventory", controllerInv.getContents());
			map.put("location", controller.getLocation());
			map.put("mode", player.getGameMode());
			reg3.setRegister(lowerName, map);
			
			controllerInv.setContents(playerInv.getContents());
			controller.teleport(player);
			player.setGameMode(GameMode.SPECTATOR);
			
			disguiseHelper.disguiseAsPlayer(controller, player);
		}
	}
}
