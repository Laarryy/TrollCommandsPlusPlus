package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidTypeException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.databases.VegetableTypeSearchDatabase;
import me.egg82.tcpp.services.registries.VegetableNameRegistry;
import me.egg82.tcpp.services.registries.VegetableRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.VegetableHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.sql.LanguageDatabase;

public class VegetableCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry<String> vegetableNameRegistry = ServiceLocator.getService(VegetableNameRegistry.class);
	private ArrayList<String> vegetableNames = new ArrayList<String>();
	
	private LanguageDatabase vegetableTypeDatabase = ServiceLocator.getService(VegetableTypeSearchDatabase.class);
	private VegetableHelper vegetableHelper = ServiceLocator.getService(VegetableHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
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
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_VEGETABLE)));
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
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
					sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
					onError().invoke(this,  new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(search)));
					return;
				}
				
				type = Material.getMaterial(types[0].toUpperCase());
				if (type == null) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
					onError().invoke(this,  new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(search)));
					return;
				}
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (!vegetableRegistry.hasRegister(uuid)) {
					if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
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
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!vegetableRegistry.hasRegister(uuid)) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
					onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
					return;
				}
				
				e(uuid, player, type);
			} else {
				eUndo(uuid, player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
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
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void eUndo(UUID uuid, Player player) {
		vegetableHelper.unvegetable(uuid, player);
		
		sender.sendMessage(player.getName() + " is no longer a vegetable.");
	}
}
