package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
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
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.utils.MathUtil;

public class StampedeCommand extends PluginCommand {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public StampedeCommand(CommandSender sender, Command command, String label, String[] args) {
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
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player);
			}
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
			
			e(player);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(Player player) {
		int numCows = MathUtil.fairRoundedRandom(10, 20);
		Location playerLocation = player.getLocation().clone();
		Location herdLocation = BlockUtil.getTopWalkableBlock(new Location(playerLocation.getWorld(), MathUtil.random(playerLocation.getX() - 5.0d, playerLocation.getX() + 5.0d), playerLocation.getY(), MathUtil.random(playerLocation.getZ() - 5.0d, playerLocation.getZ() + 5.0d)));
		Vector cowVelocity = playerLocation.toVector().subtract(herdLocation.toVector()).normalize().multiply(1.5d);
		
		for (int i = 0; i < numCows; i++) {
			spawnCow(player, herdLocation, cowVelocity);
		}
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("The angry cows have been unleashed on " + player.getName() + ".");
	}
	private void spawnCow(Player player, Location location, Vector velocity) {
		Cow c = player.getWorld().spawn(location, Cow.class);
		Silverfish f = player.getWorld().spawn(location, Silverfish.class);
		f.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 3), true);
		
		entityUtil.addPassenger(f, c);
		
		f.setVelocity(velocity);
		c.setVelocity(velocity);
		f.setTarget(player);
	}
	
	protected void onUndo() {
		
	}
}
