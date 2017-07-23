package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.NoBlockSpaceException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.utils.MathUtil;

public class BanishCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public BanishCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
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
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_BANISH)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_BANISH)));
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
		
		double banishMax = 20000.0d;
		if (args.length == 2) {
			try {
				banishMax = Double.parseDouble(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player, banishMax);
			}
			onComplete().invoke(this, CompleteEventArgs.EMPTY);
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
				onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
				return;
			}
			
			if (e(player, banishMax)) {
				onComplete().invoke(this, CompleteEventArgs.EMPTY);
			}
		}
	}
	private boolean e(Player player, double maxRadius) {
		double minRadius = maxRadius - (maxRadius / 3.0d);
		Location currentLocation = player.getLocation();
		double currentX = currentLocation.getX();
		double newX = currentX;
		double currentZ = currentLocation.getZ();
		double newZ = currentZ;
		
		while (newX >= currentX - minRadius && newX <= currentX + minRadius) {
			newX = MathUtil.random(currentX - maxRadius, currentX + maxRadius);
		}
		while (newZ >= currentZ - minRadius && newZ <= currentZ + minRadius) {
			newZ = MathUtil.random(currentZ - maxRadius, currentZ + maxRadius);
		}
		
		Location newLocation = null;
		Material headBlock = null;
		int retryCount = 0;
		
		do {
			newLocation = BlockUtil.getTopWalkableBlock(new Location(currentLocation.getWorld(), newX, MathUtil.random(5.0d, currentLocation.getWorld().getMaxHeight()), newZ));
			headBlock = newLocation.clone().add(0.0d, 1.0d, 0.0d).getBlock().getType();
			retryCount++;
		} while (headBlock != Material.AIR && retryCount <= 100);
		
		if (headBlock != Material.AIR) {
			sender.sendMessage(player.getName() + " could not be banished because there was not enough space around them!");
			onError().invoke(this, new ExceptionEventArgs<NoBlockSpaceException>(new NoBlockSpaceException(newLocation)));
			return false;
		}
		
		player.teleport(newLocation);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " has been banished.");
		return true;
	}
	
	protected void onUndo() {
		
	}
}