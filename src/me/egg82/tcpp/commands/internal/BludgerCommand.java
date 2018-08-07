package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.BludgerRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.MathUtil;

public class BludgerCommand extends CommandHandler {
	//vars
	private IVariableRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public BludgerCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_BLUDGER)) {
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
				
				if (!bludgerRegistry.hasRegister(uuid)) {
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
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!bludgerRegistry.hasRegister(uuid)) {
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
		Location playerLocation = player.getLocation();
		
		Location location = null;
		if (!sender.isConsole()) {
			if (!sender.getUuid().equals(uuid)) {
				Location senderLocation = ((Player) sender.getHandle()).getLocation();
				if (senderLocation.getWorld().getName().equals(playerLocation.getWorld().getName())) {
					if (senderLocation.distanceSquared(playerLocation) <= 900.0d) { // 30
						location = LocationUtil.getLocationInFront(senderLocation, 1.5d, true);
					}
				}
			}
		}
		
		if (location == null) {
			location = BlockUtil.getNearestAirBlock(LocationUtil.getRandomPointAround(playerLocation, MathUtil.random(5.0d, 14.0d), true), 10).add(0.5d, 0.5d, 0.5d);
		}
		
		Fireball fireball = player.getWorld().spawn(location, Fireball.class);
		Vector v = playerLocation.toVector().subtract(location.toVector()).normalize().multiply(0.35d);
		if (LocationUtil.isFinite(v)) {
			fireball.setVelocity(v);
		}
		bludgerRegistry.setRegister(uuid, fireball);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now being chased by a bludger!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (bludgerRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		}
	}
	private void eUndo(UUID uuid, Player player) {
		bludgerRegistry.getRegister(uuid, Fireball.class).remove();
		bludgerRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " is no longer being chased by a bludger.");
	}
}
