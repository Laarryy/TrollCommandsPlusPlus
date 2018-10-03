package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.core.PlayerInfoContainer;
import ninja.egg82.bukkit.reflection.uuid.IUUIDHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.pair.DoubleDoublePair;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.MathUtil;

public class BanishCommand extends CommandHandler {
	//vars
    private IUUIDHelper uuidHelper = ServiceLocator.getService(IUUIDHelper.class);

	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public BanishCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_BANISH)) {
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
		
		double banishMax = 20000.0d;
		if (args.length == 2) {
			try {
				banishMax = Double.parseDouble(args[1]);
			} catch (Exception ex) {
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
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player, banishMax);
			}
		} else {
            PlayerInfoContainer info = uuidHelper.getPlayer(args[0], true);
            if (info == null) {
                sender.sendMessage(ChatColor.RED + "Could not fetch player info. Please try again later.");
                return;
            }
			
            Player player = CommandUtil.getPlayerByUuid(info.getUuid());
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			if (player.hasPermission(PermissionsType.IMMUNE)) {
				sender.sendMessage(ChatColor.RED + "Player is immune.");
				return;
			}
			
			e(player, banishMax);
		}
	}
	private void e(Player player, double maxRadius) {
		Location currentLocation = player.getLocation();
		DoubleDoublePair maxXZ = getMax(currentLocation, maxRadius);
		DoubleDoublePair minXZ = new DoubleDoublePair(maxXZ.getLeft() - (maxXZ.getLeft() / 5.0d), maxXZ.getRight() - (maxXZ.getRight() / 5.0d));
		double currentX = currentLocation.getX();
		double newX = currentX;
		double currentZ = currentLocation.getZ();
		double newZ = currentZ;
		
		Location newLocation = null;
		Material headBlock = null;
		Material footBlock = null;
		Material belowBlock = null;
		int retryCount = 0;
		
		do {
			do {
				newX = MathUtil.random(currentX - maxXZ.getLeft(), currentX + maxXZ.getLeft());
			} while (newX >= currentX - minXZ.getLeft() && newX <= currentX + minXZ.getLeft());
			do {
				newZ = MathUtil.random(currentZ - maxXZ.getRight(), currentZ + maxXZ.getRight());
			} while (newZ >= currentZ - minXZ.getRight() && newZ <= currentZ + minXZ.getRight());
			
			newLocation = BlockUtil.getHighestSolidBlock(new Location(currentLocation.getWorld(), newX, MathUtil.random(5.0d, currentLocation.getWorld().getMaxHeight()), newZ)).add(0.0d, 1.0d, 0.0d);
			belowBlock = newLocation.clone().add(0.0d, -1.0d, 0.0d).getBlock().getType();
			footBlock = newLocation.getBlock().getType();
			headBlock = newLocation.clone().add(0.0d, 1.0d, 0.0d).getBlock().getType();
			retryCount++;
		} while (!isHospitable(headBlock, footBlock, belowBlock) && retryCount <= 100);
		
		if (!isHospitable(headBlock, footBlock, belowBlock)) {
			sender.sendMessage(ChatColor.RED + player.getName() + " could not be banished because all of the locations chosen were inhospitable!");
			return;
		}
		
		player.teleport(newLocation.add(0.5d, 0.0d, 0.5d));
		metricsHelper.commandWasRun(this);
		sender.sendMessage(player.getName() + " has been banished.");
		return;
	}
	private DoubleDoublePair getMax(Location current, double max) {
		// Get world border
		WorldBorder border = current.getWorld().getWorldBorder();
		// get border center
		Location center = border.getCenter();
		// Translate border center to player location and adjust for border size
		double borderSizeX = (border.getSize() / 2.0d) - Math.abs(current.getX() - center.getX()) - 1.0d;
		double borderSizeZ = (border.getSize() / 2.0d) - Math.abs(current.getZ() - center.getZ()) - 1.0d;
		
		// Return the min between border size and original max (effectively capping "max" at the world border if needed)
		return new DoubleDoublePair(Math.min(borderSizeX, max), Math.min(borderSizeZ, max));
	}
	private boolean isHospitable(Material headBlock, Material footBlock, Material belowBlock) {
		if (
			headBlock != Material.AIR
			|| footBlock == Material.LAVA
			|| footBlock.name().equals("STATIONARY_LAVA")
			|| footBlock.name().equals("LEGACY_STATIONARY_LAVA")
			|| (footBlock == Material.WATER && headBlock == Material.WATER)
			|| belowBlock == Material.CACTUS
			|| belowBlock.name().equals("MAGMA")
			|| belowBlock == Material.FIRE
		) {
			return false;
		}
		
		return true;
	}
	
	protected void onUndo() {
		
	}
}