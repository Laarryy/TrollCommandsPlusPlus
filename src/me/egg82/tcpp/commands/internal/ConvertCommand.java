package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidTypeException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.registries.ConvertRegistry;
import me.egg82.tcpp.services.registries.MaterialNameRegistry;
import me.egg82.tcpp.util.MetricsHelper;
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

public class ConvertCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> convertRegistry = ServiceLocator.getService(ConvertRegistry.class);
	private ArrayList<String> materialNames = new ArrayList<String>();
	private IRegistry<String> materialNameRegistry = ServiceLocator.getService(MaterialNameRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public ConvertCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		for (String key : materialNameRegistry.getKeys()) {
			if (!materialNameRegistry.hasRegister(key + "_ITEM")) {
				materialNames.add(WordUtils.capitalize(key.toLowerCase().replace('_', ' ')));
			}
		}
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
			if (args[1].isEmpty()) {
				return materialNames;
			} else {
				ArrayList<String> retVal = new ArrayList<String>();
				
				for (String name : materialNames) {
					if (name.toLowerCase().startsWith(args[1].toLowerCase())) {
						retVal.add(name);
					}
				}
				
				return retVal;
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_CONVERT)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_CONVERT)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		Material type = null;
		if (args.length == 2) {
			type = Material.getMaterial(args[1].replaceAll(" ", "_").toUpperCase() + "_ITEM");
			if (type == null) {
				type = Material.getMaterial(args[1].replaceAll(" ", "_").toUpperCase());
				if (type == null) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
					onError().invoke(this, new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(args[1])));
					return;
				}
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (type != null) {
					if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(player.getUniqueId(), player, type);
				} else {
					eUndo(player.getUniqueId(), player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			if (type != null) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
					onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
					return;
				}
				
				e(player.getUniqueId(), player, type);
			} else {
				eUndo(player.getUniqueId(), player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	@SuppressWarnings("unchecked")
	private void e(UUID uuid, Player player, Material type) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		List<ItemStack[]> conversions = null;
		
		if (convertRegistry.hasRegister(uuid)) {
			conversions = convertRegistry.getRegister(uuid, List.class);
		} else {
			conversions = new ArrayList<ItemStack[]>();
			convertRegistry.setRegister(uuid, conversions);
		}
		
		conversions.add(items.clone());
		
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getType() != Material.AIR) {
				items[i] = new ItemStack(type, items[i].getAmount());
			}
		}
		
		inv.setContents(items);
		player.updateInventory();
		
		metricsHelper.commandWasRun(this);
		
		String name = type.name().toLowerCase();
		if (name.length() > 5 && name.substring(name.length() - 5).equals("_item")) {
			name = name.substring(0, name.length() - 5);
		}
		name.replace('_', ' ');
		
		sender.sendMessage(player.getName() + "'s inventory is now " + name + ".");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (convertRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	@SuppressWarnings("unchecked")
	private void eUndo(UUID uuid, Player player) {
		if (!convertRegistry.hasRegister(uuid)) {
			sender.sendMessage("No conversions left to undo for " + player.getName() + ".");
			return;
		}
		
		List<ItemStack[]> conversions = convertRegistry.getRegister(uuid, List.class);
		player.getInventory().setContents(conversions.remove(conversions.size() - 1));
		player.updateInventory();
		if (conversions.size() == 0) {
			convertRegistry.removeRegister(uuid);
		}
		
		sender.sendMessage("Undid one conversion for " + player.getName() + ".");
	}
}
