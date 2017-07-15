package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.HotTubRegistry;
import me.egg82.tcpp.services.PortalRegistry;
import me.egg82.tcpp.services.VoidRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.WorldHoleHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class PortalCommand extends PluginCommand {
	//vars
	private IRegistry portalRegistry = ServiceLocator.getService(PortalRegistry.class);
	private IRegistry voidRegistry = ServiceLocator.getService(VoidRegistry.class);
	private IRegistry hotTubRegistry = ServiceLocator.getService(HotTubRegistry.class);
	
	private WorldHoleHelper worldHoleHelper = ServiceLocator.getService(WorldHoleHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public PortalCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_PORTAL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				String uuid = player.getUniqueId().toString();
				
				if (portalRegistry.hasRegister(uuid)) {
					continue;
				}
				if (voidRegistry.hasRegister(uuid)) {
					continue;
				}
				if (hotTubRegistry.hasRegister(uuid)) {
					continue;
				}
				
				e(uuid, player);
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
			
			if (portalRegistry.hasRegister(uuid)) {
				sender.sendMessage(MessageType.ALREADY_USED);
				dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
				return;
			}
			if (voidRegistry.hasRegister(uuid)) {
				sender.sendMessage(MessageType.ALREADY_USED);
				dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
				return;
			}
			if (hotTubRegistry.hasRegister(uuid)) {
				sender.sendMessage(MessageType.ALREADY_USED);
				dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
				return;
			}
			
			e(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		worldHoleHelper.portalHole(uuid, player);
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now falling to The(ir) End.");
	}
	
	protected void onUndo() {
		
	}
}
