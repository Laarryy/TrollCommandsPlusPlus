package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.databases.VegetableTypeSearchDatabase;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.VegetableNameRegistry;
import me.egg82.tcpp.registries.VegetableRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.VegetableHelper;
import ninja.egg82.bukkit.reflection.material.IMaterialHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.sql.LanguageDatabase;

public class VegetableCommand extends CommandHandler {
	//vars
	private IVariableRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IVariableRegistry<String> vegetableNameRegistry = ServiceLocator.getService(VegetableNameRegistry.class);
	private ArrayList<String> vegetableNames = new ArrayList<String>();
	
	private LanguageDatabase vegetableTypeDatabase = ServiceLocator.getService(VegetableTypeSearchDatabase.class);
	private VegetableHelper vegetableHelper = ServiceLocator.getService(VegetableHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	private Material potato = ServiceLocator.getService(IMaterialHelper.class).getByName("POTATO_ITEM");
	
	//constructor
	public VegetableCommand() {
		super();
		
		for (String key : vegetableNameRegistry.getKeys()) {
			vegetableNames.add(vegetableNameRegistry.getRegister(key, String.class));
		}
	}
	
	//public
	public List<String> tabComplete() {
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
			}
			
			ArrayList<String> retVal = new ArrayList<String>();
			
			for (int i = 0; i < vegetableNames.size(); i++) {
				if (vegetableNames.get(i).toLowerCase().startsWith(args[1].toLowerCase())) {
					retVal.add(vegetableNames.get(i));
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_VEGETABLE)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		Material type = potato;
		
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
					sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
					return;
				}
				
				type = Material.getMaterial(types[0].toUpperCase());
				if (type == null) {
					sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
					return;
				}
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (!vegetableRegistry.hasRegister(uuid)) {
					if (player.hasPermission(PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(uuid, player, type);
				} else {
					eUndo(uuid, player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!vegetableRegistry.hasRegister(uuid)) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
					return;
				}
				
				e(uuid, player, type);
			} else {
				eUndo(uuid, player);
			}
		}
	}
	private void e(UUID uuid, Player player, Material vegetable) {
		vegetableHelper.vegetable(uuid, player, vegetable);
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now a vegetable.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (vegetableRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		}
	}
	private void eUndo(UUID uuid, Player player) {
		vegetableHelper.unvegetable(uuid, player);
		
		sender.sendMessage(player.getName() + " is no longer a vegetable.");
	}
}
