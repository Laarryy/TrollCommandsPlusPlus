package me.egg82.tcpp.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlInventoryRegistry;
import me.egg82.tcpp.services.ControlModeRegistry;
import me.egg82.tcpp.services.ControlRegistry;
import me.egg82.tcpp.util.IDisguiseHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlCommand extends PluginCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	private IRegistry controlModeRegistry = (IRegistry) ServiceLocator.getService(ControlModeRegistry.class);
	private IRegistry controlInventoryRegistry = (IRegistry) ServiceLocator.getService(ControlInventoryRegistry.class);
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(IDisguiseHelper.class);
	
	//constructor
	public ControlCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_CONTROL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!disguiseHelper.isValidLibrary()) {
			sender.sendMessage(MessageType.NO_LIBRARY);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(SpigotMessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.CONSOLE_NOT_ALLOWED);
			return;
		}
		
		Player controller = (Player) sender;
		String controllerUuid = controller.getUniqueId().toString();
		
		if (args.length == 0) {
			if (!controlRegistry.hasRegister(controllerUuid)) {
				sender.sendMessage(MessageType.NOT_CONTROLLING);
				dispatch(CommandEvent.ERROR, CommandErrorType.NOT_CONTROLLING);
				return;
			}
			
			undo(controllerUuid, controller);
		} else if (args.length == 1) {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(MessageType.PLAYER_IMMUNE);
				dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
				return;
			}
			
			String playerUuid = player.getUniqueId().toString();
			
			if (controllerUuid == playerUuid) {
				sender.sendMessage(MessageType.NO_CONTROL_SELF);
				dispatch(CommandEvent.ERROR, CommandErrorType.NO_CONTROL_SELF);
				return;
			}
			
			Player controlledPlayer = (Player) controlRegistry.getRegister(controllerUuid);
			if (controlledPlayer != null) {
				undo(controllerUuid, controller);
				if (controlledPlayer.getUniqueId().toString() == playerUuid) {
					dispatch(CommandEvent.COMPLETE, null);
					return;
				}
			}
			
			e(controller.getUniqueId().toString(), controller, playerUuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String controllerUuid, Player controller, String uuid, Player player) {
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
	private void undo(String controllerUuid, Player controller) {
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
}
