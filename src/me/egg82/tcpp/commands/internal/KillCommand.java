package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.KillRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class KillCommand extends PluginCommand {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IRegistry killRegistry = ServiceLocator.getService(KillRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public KillCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_KILL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		long delay = 10L;
		if (args.length == 2) {
			try {
				delay = Long.parseLong(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
			
			if (delay < 0L) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				String uuid = player.getUniqueId().toString();
				
				if (!killRegistry.hasRegister(uuid)) {
					e(uuid, player, delay);
				} else {
					eUndo(uuid, player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(MessageType.PLAYER_IMMUNE);
				dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
				return;
			}
			
			String uuid = player.getUniqueId().toString();
			
			if (!killRegistry.hasRegister(uuid)) {
				e(uuid, player, delay);
			} else {
				eUndo(uuid, player);
			}
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, long delay) {
		killRegistry.setRegister(uuid, Player.class, player);
		
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
		String uuid = player.getUniqueId().toString();
		
		if (killRegistry.hasRegister(uuid)) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void eUndo(String uuid, Player player) {
		killRegistry.setRegister(uuid, Player.class, null);
		
		sender.sendMessage(player.getName() + "'s death is no longer inevitable.");
	}
}
