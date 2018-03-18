package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidTypeException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.registries.FillRegistry;
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

public class FillCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> fillRegistry = ServiceLocator.getService(FillRegistry.class);
	private ArrayList<String> materialNames = new ArrayList<String>();
	private IRegistry<String> materialNameRegistry = ServiceLocator.getService(MaterialNameRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public FillCommand() {
		super();
		
		for (String key : materialNameRegistry.getKeys()) {
			if (!materialNameRegistry.hasRegister(key + "_ITEM")) {
				materialNames.add(WordUtils.capitalize(key.toLowerCase().replace('_', ' ')));
			}
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_FILL)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_FILL)));
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
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void e(UUID uuid, Player player, Material type) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		List<ItemStack[]> fills = null;
		
		if (fillRegistry.hasRegister(uuid)) {
			fills = fillRegistry.getRegister(uuid, List.class);
		} else {
			fills = new ArrayList<ItemStack[]>();
			fillRegistry.setRegister(uuid, fills);
		}
		
		fills.add(items.clone());
		
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null || items[i].getType() == Material.AIR) {
				items[i] = new ItemStack(type, type.getMaxStackSize());
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
		
		sender.sendMessage(player.getName() + "'s inventory is now filled with " + name + "s!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (fillRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void eUndo(UUID uuid, Player player) {
		if (!fillRegistry.hasRegister(uuid)) {
			sender.sendMessage("No fills left to undo for " + player.getName() + ".");
			return;
		}
		
		List<ItemStack[]> fills = fillRegistry.getRegister(uuid, List.class);
		player.getInventory().setContents(fills.remove(fills.size() - 1));
		player.updateInventory();
		if (fills.size() == 0) {
			fillRegistry.removeRegister(uuid);
		}
		
		sender.sendMessage("Undid one fill for " + player.getName() + ".");
	}
}
