package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableNameRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import me.egg82.tcpp.services.VegetableTypeSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.VegetableHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.sql.LanguageDatabase;

public class VegetableCommand extends PluginCommand {
	//vars
	private IRegistry vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry vegetableNameRegistry = ServiceLocator.getService(VegetableNameRegistry.class);
	private ArrayList<String> vegetableNames = new ArrayList<String>();
	
	private LanguageDatabase vegetableTypeDatabase = ServiceLocator.getService(VegetableTypeSearchDatabase.class);
	private VegetableHelper vegetableHelper = ServiceLocator.getService(VegetableHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public VegetableCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		vegetableNames.add("Beetroot");
		vegetableNames.add("Brown Mushroom");
		vegetableNames.add("Carrot");
		vegetableNames.add("Potato");
		vegetableNames.add("Red Mushroom");
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
		} else if (args.length == 2) {
			if (args[1].isEmpty()) {
				return vegetableNames;
			} else {
				ArrayList<String> retVal = new ArrayList<String>();
				
				for (int i = 0; i < vegetableNames.size(); i++) {
					if (vegetableNames.get(i).toLowerCase().startsWith(args[1].toLowerCase())) {
						retVal.add(vegetableNames.get(i));
					}
				}
				
				return retVal;
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_VEGETABLE)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Material type = Material.POTATO_ITEM;
		
		if (args.length > 1) {
			String search = "";
			for (int i = 1; i < args.length; i++) {
				search += args[i] + " ";
			}
			search = search.trim();
			
			type = Material.getMaterial(search.replaceAll(" ", "_").toUpperCase());
			
			if (type == null || !vegetableNameRegistry.hasRegister(type.name())) {
				// Type not found or not allowed. Search the database
				String[] types = vegetableTypeDatabase.getValues(vegetableTypeDatabase.naturalLanguage(search, false), 0);
				
				if (types == null || types.length == 0) {
					sender.sendMessage(MessageType.VEGETABLE_NOT_FOUND);
					dispatch(CommandEvent.ERROR, CommandErrorType.VEGETABLE_NOT_FOUND);
					return;
				}
				
				type = Material.getMaterial(types[0].toUpperCase());
				if (type == null) {
					sender.sendMessage(MessageType.VEGETABLE_NOT_FOUND);
					dispatch(CommandEvent.ERROR, CommandErrorType.VEGETABLE_NOT_FOUND);
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
				
				String uuid = player.getUniqueId().toString();
				
				if (!vegetableRegistry.hasRegister(uuid)) {
					e(uuid, player, type);
				} else {
					eUndo(uuid, player);
				}
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
			
			String uuid = player.getUniqueId().toString();
			
			if (!vegetableRegistry.hasRegister(uuid)) {
				e(uuid, player, type);
			} else {
				eUndo(uuid, player);
			}
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, Material vegetable) {
		vegetableHelper.vegetable(uuid, player, vegetable);
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now a vegetable.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (vegetableRegistry.hasRegister(uuid)) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void eUndo(String uuid, Player player) {
		vegetableHelper.unvegetable(uuid, player);
		
		sender.sendMessage(player.getName() + " is no longer a vegetable.");
	}
}
