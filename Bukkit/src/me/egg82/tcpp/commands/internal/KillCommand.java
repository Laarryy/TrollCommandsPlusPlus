package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.KillRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class KillCommand extends CommandHandler {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IVariableRegistry<UUID> killRegistry = ServiceLocator.getService(KillRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public KillCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_KILL)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		long delay = 10L;
		if (args.length == 2) {
			try {
				delay = Long.parseLong(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
				return;
			}
			
			if (delay < 0L) {
				sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (!killRegistry.hasRegister(uuid)) {
					if (player.hasPermission(PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(uuid, player, delay);
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
					if (killRegistry.hasRegister(uuid)) {
						eUndo(uuid, offlinePlayer);
						return;
					}
				}
				
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!killRegistry.hasRegister(uuid)) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
					return;
				}
				
				e(uuid, player, delay);
			} else {
				eUndo(uuid, player);
			}
		}
	}
	private void e(UUID uuid, Player player, long delay) {
		killRegistry.setRegister(uuid, null);
		
		// Wait Xx20 ticks
		TaskUtil.runSync(new Runnable() {
			public void run() {
				// Is the player still in the registry, or have they been removed for one reason or another?
				if (killRegistry.hasRegister(uuid)) {
					// Kill them!
					player.setHealth(0.0d);
					entityUtil.damage(player, EntityDamageEvent.DamageCause.SUICIDE, Double.MAX_VALUE);
				}
			}
		}, delay * 20);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + "'s death is inevitable.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (killRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		} else {
			OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
			UUID uuid = offlinePlayer.getUniqueId();
			if (killRegistry.hasRegister(uuid)) {
				eUndo(uuid, offlinePlayer);
			}
		}
	}
	private void eUndo(UUID uuid, Player player) {
		killRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + "'s death is no longer inevitable.");
	}
	private void eUndo(UUID uuid, OfflinePlayer player) {
		killRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + "'s death is no longer inevitable.");
	}
}
