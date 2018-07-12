package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.MaterialNameRegistry;
import me.egg82.tcpp.registries.MidasTouchRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.patterns.tuples.pair.Boolean2Pair;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.protocol.reflection.IFakeBlockHelper;

public class MidasTouchCommand extends CommandHandler {
	//vars
	private IRegistry<UUID, Boolean2Pair<Material>> midasTouchRegistry = ServiceLocator.getService(MidasTouchRegistry.class);
	private ArrayList<String> materialNames = new ArrayList<String>();
	private IVariableRegistry<String> materialNameRegistry = ServiceLocator.getService(MaterialNameRegistry.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public MidasTouchCommand() {
		super();
		
		for (String key : materialNameRegistry.getKeys()) {
			if (key.length() < 5 || !key.substring(key.length() - 5).equalsIgnoreCase("_item")) {
				materialNames.add(key.toLowerCase());
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
			}
			
			ArrayList<String> retVal = new ArrayList<String>();
			
			for (String name : materialNames) {
				if (name.toLowerCase().startsWith(args[1].toLowerCase())) {
					retVal.add(name);
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_MIDAS_TOUCH)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2, 3)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		Material type = Material.GOLD_BLOCK;
		if (args.length >= 2) {
			type = Material.matchMaterial(args[1]);
			if (type == null) {
				sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
				return;
			}
		}
		boolean real = true;
		if (args.length >= 3) {
			real = Boolean.parseBoolean(args[2]);
			if (!real && !fakeBlockHelper.isValidLibrary()) {
				sender.sendMessage(ChatColor.RED + "The options you provided attempted to use a library that was not installed. Please use different options or install the required library and restart the server.");
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (!midasTouchRegistry.hasRegister(uuid)) {
					if (player.hasPermission(PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(uuid, player, type, real);
				} else {
					eUndo(uuid, player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
				if (offlinePlayer != null) {
					UUID uuid = offlinePlayer.getUniqueId();
					if (midasTouchRegistry.hasRegister(uuid)) {
						eUndo(uuid, offlinePlayer);
						return;
					}
				}
				
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!midasTouchRegistry.hasRegister(uuid)) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
					return;
				}
				
				e(uuid, player, type, real);
			} else {
				eUndo(uuid, player);
			}
		}
	}
	private void e(UUID uuid, Player player, Material material, boolean real) {
		midasTouchRegistry.setRegister(uuid, new Boolean2Pair<Material>(material, real));
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now King Midas." + ((real) ? "" : " (Note that you won't be able to see block changes, but they can)"));
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (midasTouchRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		} else {
			OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
			UUID uuid = offlinePlayer.getUniqueId();
			if (midasTouchRegistry.hasRegister(uuid)) {
				eUndo(uuid, offlinePlayer);
			}
		}
	}
	private void eUndo(UUID uuid, Player player) {
		midasTouchRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " is no longer King Midas.");
	}
	private void eUndo(UUID uuid, OfflinePlayer player) {
		midasTouchRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " is no longer King Midas.");
	}
}
