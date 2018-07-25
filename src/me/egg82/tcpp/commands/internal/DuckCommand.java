package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.DuckRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class DuckCommand extends CommandHandler {
	//vars
	private IVariableRegistry<UUID> duckRegistry = ServiceLocator.getService(DuckRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
	
	//constructor
	public DuckCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_DUCK)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!disguiseHelper.isValidLibrary()) {
			sender.sendMessage(ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.");
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
				
				if (!duckRegistry.hasRegister(uuid)) {
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
			
			if (!duckRegistry.hasRegister(uuid)) {
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
		disguiseHelper.disguiseAsEntity(player, EntityType.CHICKEN, true);
		duckRegistry.setRegister(uuid, null);
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now a duckchicken.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (duckRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		}
	}
	private void eUndo(UUID uuid, Player player) {
		disguiseHelper.undisguise(player);
		duckRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " is no longer a duckchicken.");
	}
}
