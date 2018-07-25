package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.StampedeRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.MathUtil;

public class StampedeCommand extends CommandHandler {
	//vars
	private IVariableRegistry<UUID> stampedeRegistry = ServiceLocator.getService(StampedeRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public StampedeCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_STAMPEDE)) {
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
				
				if (!stampedeRegistry.hasRegister(uuid)) {
					if (player.hasPermission(PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(uuid, player);
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
					if (stampedeRegistry.hasRegister(uuid)) {
						eUndo(uuid, offlinePlayer);
						return;
					}
				}
				
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!stampedeRegistry.hasRegister(uuid)) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
					return;
				}
				
				e(uuid, player);
			} else {
				eUndo(uuid, player);
			}
		}
	}
	private void e(UUID uuid, Player player) {
		ArrayList<Creature> entities = new ArrayList<Creature>();
		
		int numCows = MathUtil.fairRoundedRandom(10, 20);
		Location playerLocation = player.getLocation();
		Location herdLocation = BlockUtil.getHighestSolidBlock(new Location(playerLocation.getWorld(), MathUtil.random(playerLocation.getX() - 5.0d, playerLocation.getX() + 5.0d), playerLocation.getY(), MathUtil.random(playerLocation.getZ() - 5.0d, playerLocation.getZ() + 5.0d))).add(0.0d, 1.0d, 0.0d);
		
		for (int i = 0; i < numCows; i++) {
			Cow c = player.getWorld().spawn(herdLocation, Cow.class);
			c.setTarget(player);
			Vector v = player.getLocation().toVector().subtract(herdLocation.toVector()).normalize().multiply(0.23d);
			if (LocationUtil.isFinite(v)) {
				c.setVelocity(v);
			}
			entities.add(c);
		}
		
		stampedeRegistry.setRegister(uuid, entities);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("The angry cows have been unleashed on " + player.getName() + ".");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (stampedeRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		} else {
			OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
			UUID uuid = offlinePlayer.getUniqueId();
			if (stampedeRegistry.hasRegister(uuid)) {
				eUndo(uuid, offlinePlayer);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void eUndo(UUID uuid, Player player) {
		List<Creature> entities = stampedeRegistry.getRegister(uuid, List.class);
		
		for (Creature e : entities) {
			e.remove();
		}
		
		stampedeRegistry.removeRegister(uuid);
		
		sender.sendMessage("The angry cows have subsided for " + player.getName() + ".");
	}
	@SuppressWarnings("unchecked")
	private void eUndo(UUID uuid, OfflinePlayer player) {
		List<Creature> entities = stampedeRegistry.getRegister(uuid, List.class);
		
		for (Creature e : entities) {
			e.remove();
		}
		
		stampedeRegistry.removeRegister(uuid);
		
		sender.sendMessage("The angry cows have subsided for " + player.getName() + ".");
	}
}
