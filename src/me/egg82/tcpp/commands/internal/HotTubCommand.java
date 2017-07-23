package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.CommandInUseException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.HoleRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.WorldHoleHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class HotTubCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> holeRegistry = ServiceLocator.getService(HoleRegistry.class);
	
	private WorldHoleHelper worldHoleHelper = ServiceLocator.getService(WorldHoleHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public HotTubCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_HOT_TUB)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_HOT_TUB)));
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
				
				UUID uuid = player.getUniqueId();
				
				if (holeRegistry.hasRegister(uuid)) {
					continue;
				}
				
				e(uuid, player);
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
			
			UUID uuid = player.getUniqueId();
			
			if (holeRegistry.hasRegister(uuid)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.COMMAND_IN_USE));
				onError().invoke(this, new ExceptionEventArgs<CommandInUseException>(new CommandInUseException(this)));
				return;
			}
			
			e(uuid, player);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(UUID uuid, Player player) {
		worldHoleHelper.hotTubHole(uuid, player);
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now taking a hot bath.");
	}
	
	protected void onUndo() {
		
	}
}
