package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class SwapCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public SwapCommand() {
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
		} else if (args.length == 2) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[1].isEmpty()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					retVal.add(player.getName());
				}
			} else {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_SWAP)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_SWAP)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 2)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		List<Player> players1 = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		List<Player> players2 = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[1], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		Player player1 = null;
		Player player2 = null;
		
		if (players1.size() == 0) {
			player1 = CommandUtil.getPlayerByName(args[0]);
			if (player1 == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
		} else {
			player1 = players1.get(0);
		}
		if (players2.size() == 0) {
			player2 = CommandUtil.getPlayerByName(args[1]);
			if (player2 == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
		} else {
			player2 = players2.get(0);
		}
		if (CommandUtil.hasPermission(player1, PermissionsType.IMMUNE)) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
			onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player1)));
			return;
		}
		if (CommandUtil.hasPermission(player2, PermissionsType.IMMUNE)) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
			onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player2)));
			return;
		}
		
		e(player1, player2);
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(Player player1, Player player2) {
		Location player1Location = player1.getLocation().clone();
		Vector player1Velocity = player1.getVelocity();
		Location player2Location = player2.getLocation().clone();
		Vector player2Velocity = player2.getVelocity();
		
		player1.teleport(player2Location);
		player1.setVelocity(player2Velocity);
		player2.teleport(player1Location);
		player2.setVelocity(player1Velocity);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player1.getName() + " and " + player2.getName() + "have been swapped.");
	}
	
	protected void onUndo() {
		
	}
}
