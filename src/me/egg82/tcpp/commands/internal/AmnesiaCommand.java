package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.AmnesiaRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.core.PlayerInfoContainer;
import ninja.egg82.bukkit.reflection.uuid.IUUIDHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.DynamicConcurrentDeque;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class AmnesiaCommand extends CommandHandler {
	//vars
	private IRegistry<UUID, IConcurrentDeque<String>> amnesiaRegistry = ServiceLocator.getService(AmnesiaRegistry.class);
	
	private IUUIDHelper uuidHelper = ServiceLocator.getService(IUUIDHelper.class);
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AmnesiaCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_AMNESIA)) {
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
				if (!amnesiaRegistry.hasRegister(player.getUniqueId())) {
					if (player.hasPermission(PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(player.getUniqueId(), player.getName());
				} else {
					eUndo(player.getUniqueId(), player.getName());
				}
			}
		} else {
			PlayerInfoContainer info = uuidHelper.getPlayer(args[0], true);
			if (info == null) {
				sender.sendMessage(ChatColor.RED + "Could not fetch player info. Please try again later.");
				return;
			}
			
			Player player = CommandUtil.getPlayerByUuid(info.getUuid());
			if (player == null) {
				if (amnesiaRegistry.hasRegister(info.getUuid())) {
					eUndo(info.getUuid(), info.getName());
					return;
				}
				
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			if (!amnesiaRegistry.hasRegister(info.getUuid())) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
					return;
				}
				
				e(info.getUuid(), info.getName());
			} else {
				eUndo(info.getUuid(), info.getName());
			}
		}
	}
	private void e(UUID uuid, String name) {
		amnesiaRegistry.setRegister(uuid, new DynamicConcurrentDeque<String>());
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(ChatColor.GREEN + name + " is now an amnesiac.");
	}
	
	protected void onUndo() {
		PlayerInfoContainer info = uuidHelper.getPlayer(args[0], true);
		if (info == null) {
			sender.sendMessage(ChatColor.RED + "Could not fetch player info. Please try again later.");
			return;
		}
		
		if (amnesiaRegistry.hasRegister(info.getUuid())) {
			eUndo(info.getUuid(), info.getName());
		}
	}
	private void eUndo(UUID uuid, String name) {
		amnesiaRegistry.removeRegister(uuid);
		
		sender.sendMessage(ChatColor.GREEN + name + " is no longer an amnesiac.");
	}
}
