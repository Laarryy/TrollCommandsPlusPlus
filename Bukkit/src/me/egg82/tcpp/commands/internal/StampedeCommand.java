package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.registries.StampedeRegistry;
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
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class StampedeCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> stampedeRegistry = ServiceLocator.getService(StampedeRegistry.class);
	
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_STAMPEDE)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_STAMPEDE)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (!stampedeRegistry.hasRegister(uuid)) {
					if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
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
						onComplete().invoke(this, CompleteEventArgs.EMPTY);
						return;
					}
				}
				
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!stampedeRegistry.hasRegister(uuid)) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
					onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
					return;
				}
				
				e(uuid, player);
			} else {
				eUndo(uuid, player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(UUID uuid, Player player) {
		ArrayList<Creature> entities = new ArrayList<Creature>();
		
		int numCows = MathUtil.fairRoundedRandom(10, 20);
		Location playerLocation = player.getLocation();
		Location herdLocation = BlockUtil.getTopWalkableBlock(new Location(playerLocation.getWorld(), MathUtil.random(playerLocation.getX() - 5.0d, playerLocation.getX() + 5.0d), playerLocation.getY(), MathUtil.random(playerLocation.getZ() - 5.0d, playerLocation.getZ() + 5.0d)));
		
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
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
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
