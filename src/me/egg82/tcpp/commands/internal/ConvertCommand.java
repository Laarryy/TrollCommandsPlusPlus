package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.MaterialNameRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class ConvertCommand extends PluginCommand {
	//vars
	private IRegistry materialNameRegistry = ServiceLocator.getService(MaterialNameRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public ConvertCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[0].isEmpty()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					retVal.add(player.getName());
				}
			} else {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						retVal.add(player.getName());
					}
				}
			}
			
			return retVal;
		} else if(args.length == 2) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[1].isEmpty()) {
				for (String name : materialNameRegistry.getRegistryNames()) {
					retVal.add(materialNameRegistry.getRegister(name, String.class));
				}
			} else {
				for (String name : materialNameRegistry.getRegistryNames()) {
					String value = materialNameRegistry.getRegister(name, String.class);
					if (value.toLowerCase().startsWith(args[1].toLowerCase())) {
						retVal.add(value);
					}
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_CONVERT)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Material type = Material.POTATO_ITEM;
		if (args.length == 2) {
			type = Material.getMaterial(args[1].replaceAll(" ", "_").toUpperCase());
			if (type == null) {
				type = Material.getMaterial(args[1].replaceAll(" ", "_").toUpperCase() + "_ITEM");
				if (type == null) {
					sender.sendMessage(MessageType.MATERIAL_NOT_FOUND);
					dispatch(CommandEvent.ERROR, CommandErrorType.MATERIAL_NOT_FOUND);
					return;
				}
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player.getUniqueId().toString(), player, type);
			}
		} else {
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
			
			e(player.getUniqueId().toString(), player, type);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, Material type) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getType() != Material.AIR) {
				items[i] = new ItemStack(type, items[i].getAmount());
			}
		}
		
		inv.setContents(items);
		
		metricsHelper.commandWasRun(this);
		
		String name = type.name().toLowerCase();
		if (name.substring(name.length() - 5) == "_item") {
			name = name.substring(0, name.length() - 5);
		}
		name.replaceAll("_", " ");
		
		sender.sendMessage(player.getName() + "'s inventory is now " + name + ".");
	}
	
	protected void onUndo() {
		
	}
}
