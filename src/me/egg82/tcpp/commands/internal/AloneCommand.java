package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.lists.AloneSet;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.reflection.player.IPlayerHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class AloneCommand extends CommandHandler {
	//vars
	private IConcurrentSet<UUID> aloneSet = ServiceLocator.getService(AloneSet.class);
	
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AloneCommand() {
		super();
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
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_ALONE)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					if (aloneSet.remove(uuid)) {
						eUndo(player);
					}
					continue;
				}
				
				if (aloneSet.add(uuid)) {
					e(player);
				} else {
					eUndo(player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			if (player == null) {
				OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
				if (offlinePlayer != null) {
					if (aloneSet.remove(offlinePlayer.getUniqueId())) {
						eUndo(offlinePlayer);
						return;
					}
				}
				
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (player.hasPermission(PermissionsType.IMMUNE)) {
				if (aloneSet.remove(uuid)) {
					eUndo(player);
				} else {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
				}
				return;
			}
			
			if (aloneSet.add(uuid)) {
				e(player);
			} else {
				eUndo(player);
			}
		}
	}
	private void e(Player player) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			playerHelper.hidePlayer(player, p);
		}
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(ChatColor.GREEN + player.getName() + " is now all alone :(");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (aloneSet.remove(uuid)) {
				eUndo(player);
			}
		} else {
			OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
			UUID uuid = offlinePlayer.getUniqueId();
			if (aloneSet.remove(uuid)) {
				eUndo(offlinePlayer);
			}
		}
	}
	private void eUndo(Player player) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			playerHelper.showPlayer(player, p);
		}
		
		sender.sendMessage(ChatColor.GREEN + player.getName() + " is no longer alone in this wold!");
	}
	private void eUndo(OfflinePlayer player) {
		sender.sendMessage(ChatColor.GREEN + player.getName() + " is no longer alone in this wold!");
	}
}
